package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import it.polito.atlas.alea2.TrackVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swt.VideoComponent;

public class SWTPlayer implements it.polito.atlas.alea2.Player {
	
	public List <PlayBin2> videos = new ArrayList<PlayBin2>();
	public List <Shell> shells = new ArrayList<Shell>();
	public List <TrackVideo> tracks = new ArrayList<TrackVideo>();
	long maxDuration=0;
	State state;
	
    /**
     * Returns a time String
     * @param time
     * The time in milliseconds
     * @return
     * The String representing the time
     */
    static public String timeString(long time)
    {
		if (time == -1)
			time=0;
		long millisecs = time;
        long secs = millisecs / 1000;
        int mins = (int)(secs / 60);

        millisecs = millisecs - (secs * 1000);
        secs = secs - (mins * 60);
        if (mins >= 60)
        {
        	
            int hours = (int)(mins / 60);
            mins = mins - (hours * 60);
            return String.format("%05dh:%02d:%02d.%03d", hours, mins, secs, millisecs);
        }
        return String.format("%02d:%02d.%03d", mins, secs, millisecs);
    }

    public void addVideo(TrackVideo tv) {
    	createVideoWindow(tv.getName());
    	tracks.add(tv);
    }

    private void createVideoWindow(String file) {
    	createVideoWindow(file, -1);
    }
    
    private void createVideoWindow(String file, int videodesiredindex) {
		PlayBin2 playbin;
		Shell shell;

		if (videodesiredindex==-1)
			videodesiredindex = shells.size();
		final int index=videodesiredindex;
		final String title = "SWT Video #";
		
		try {
			
			playbin = new PlayBin2("VideoPlayer");
			shell = new Shell(display());
			try {
				playbin.setInputFile(new File(file));
			} catch (Exception e) {
				System.out.print("Can't open file: " + file);
			}

			shell.setData(index);
			shell.setText(title + (index+1) + " - " + file);
			shell.addShellListener(new ShellListener() {

				@Override
				public void shellIconified(ShellEvent e) {
					//pause();
				}

				@Override
				public void shellDeiconified(ShellEvent e) {
					//play();
				}

				@Override
				public void shellDeactivated(ShellEvent e) {
				}

				@Override
				public void shellClosed(ShellEvent e) {
					//int index=(Integer) ((Shell)e.getSource()).getData();
					System.out.println("Closing #" + index);
					PlayBin2 p = videos.get(index);
					if (p != null) {
						p.stop();
						p.dispose();
						videos.set(index, null);
					}
					queryEndTime();
					shells.set(index, null);
				}

				@Override
				public void shellActivated(ShellEvent e) {
				}
			});
			
			shell.setSize(640, 480);
			shell.setLayout(new FillLayout());

			final VideoComponent component = new VideoComponent(shell, SWT.NONE);
			component.getElement().setName("Video #" + index);
			component.setKeepAspect(true);
			// shell.setSize(component.getSize());
			// component.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			// true));//GridData.FILL_BOTH));

			playbin.setVideoSink(component.getElement());
			playbin.pause();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		if (videodesiredindex == shells.size()) {
			shells.add(shell);
			videos.add(playbin);
		} else {
			shells.set(index, shell);
			videos.set(index, playbin);
		}
	}
	
	@Override
	public void dispose() {
		for (Shell s : shells)
			if (s != null)
				s.close();
	}

	@Override
	public long getEndTime() {
		if (maxDuration==0)
			return queryEndTime();
		else
			return maxDuration;
	}
	
	public long queryEndTime() {
		long duration;
		for (PlayBin2 p : videos)
			if (p != null) {
				duration = p.queryDuration(TimeUnit.MILLISECONDS);
				if (duration>maxDuration)
					maxDuration=duration;
			}
		return maxDuration;
	}

	@Override
	public long getPosition() {
		for (PlayBin2 p : videos) {
			if (p != null) {
				return p.queryPosition(TimeUnit.MILLISECONDS);
			}
		}
		return 0;
	}

	@Override
	public void open() {
		for (Shell s : shells) {
			if (s!=null)
				s.open();
		}
	}

	@Override
	public void open(TrackVideo tv) {
		int i=0;
		boolean newTrack=true;
		for (TrackVideo t : tracks) {
			if (t.equals(tv)) {
				if (shells.get(i)==null)
					createVideoWindow(tv.getName(), i);
					newTrack=false;
					break;
			}
		}
		if (newTrack) {
			addVideo(tv);
		}
		seek(getPosition());
	}

	@Override
	public void pause() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.pause();
		state=State.PAUSED;
	}

	@Override
	public void play() {
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.play();
			}
		}
		state=State.PLAYING;
	}
	
	public void resync() {
		if (state==State.PLAYING) {
			pause();
			seek(getPosition());
			play();
		} else {
			seek(getPosition());
		}
	}
	
	@Override
	public void seek(long position) {
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.seek(position, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public void stop() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.stop();
	}

	public void iconify(boolean b) {
		for (Shell s : shells)
			if (s != null)
				s.setMinimized(b);
	}

}
