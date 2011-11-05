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
	
	/**
	 * The list of opened video(s) through the GStreamer library
	 */
	public List <PlayBin2> videos = new ArrayList<PlayBin2>();
	
	/**
	 * The list of video windows
	 */
	public List <Shell> shells = new ArrayList<Shell>();
	
	/**
	 * The List of VideoTracks
	 */
	public List <TrackVideo> tracks = new ArrayList<TrackVideo>();
	
	/**
	 * The max length of video(s)
	 */
	long maxDuration=0;
	
	/**
	 * The video(s) actual state
	 */
	State state;
	
    /**
     * Returns a time String
     * @param time The time in milliseconds
     * @return The String representing the time
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

    /**
     * Adds a video to this Player
     * @param tv The TrackVideo to open
     */
    public void addVideo(TrackVideo tv) {
    	createVideoWindow(tv.getName());
    	tracks.add(tv);
    }

    /**
     * Create and initialize a new window containing the video
     * The video will be inserted at the last position of opened video list
     * @param file The file path of video
     */
    private void createVideoWindow(String file) {
    	createVideoWindow(file, -1);
    }
    
    /**
     * Create and initialize a new window containing the video
     * The video will be inserted at the desired position inside the openedvideo list
     * @param file The file path of video
     * @param videodesiredindex
     */
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
				}

				@Override
				public void shellDeiconified(ShellEvent e) {
				}

				@Override
				public void shellDeactivated(ShellEvent e) {
				}

				/**
				 * When dispose the video window dispose also the GStreamer player
				 */
				@Override
				public void shellClosed(ShellEvent e) {
					System.out.println("Closing #" + index);
					PlayBin2 p = videos.get(index);
					if (p != null) {
						p.stop();
						p.dispose();
						// set this GStreamer player as closed
						videos.set(index, null);
					}
					// Recalculate the max duration
					queryEndTime();
					// set this window as closed
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
	
    /**
     * When dispose the Player dispose also all the linked video windows
     */
	@Override
	public void dispose() {
		for (Shell s : shells)
			if (s != null)
				s.close();
	}

	/**
	 * Get the max duration time of video(s) in milliseconds
	 */
	@Override
	public long getEndTime() {
		if (maxDuration==0)
			return queryEndTime();
		else
			return maxDuration;
	}
	
	/**
	 * Check the max duration time of video(s) in milliseconds
	 */
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

	/**
	 * Get the actual time position of video(s) in milliseconds
	 */
	@Override
	public long getPosition() {
		for (PlayBin2 p : videos) {
			if (p != null) {
				return p.queryPosition(TimeUnit.MILLISECONDS);
			}
		}
		return 0;
	}

	/**
	 * Open all video(s) windows
	 */
	@Override
	public void open() {
		for (Shell s : shells) {
			if (s!=null)
				s.open();
		}
	}

	/**
	 * Open the video window by the parameter
	 * @param tv The TrackVideo to open
	 */
	@Override
	public void open(TrackVideo tv) {
		int i=0;
		boolean newTrack=true;
		Shell s = null;
		// Check if the video is already in list
		for (TrackVideo t : tracks) {
			if (t.equals(tv)) {
				s = shells.get(i);
				// Check if the Window was closed, create it
				if (s == null)
					createVideoWindow(tv.getName(), i);
				newTrack=false;
				break;
			}
		}
		if (newTrack) {
			addVideo(tv);
			s = shells.get(shells.size()-1);
		}
		s.open();
		seek(getPosition());
	}

	/**
	 * Pause all video(s)
	 */
	@Override
	public void pause() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.pause();
		state=State.PAUSED;
	}

	/**
	 * Play all video(s)
	 */
	@Override
	public void play() {
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.play();
			}
		}
		state=State.PLAYING;
	}
	
	/**
	 * Resync all video(s)
	 */
	public void resync() {
		if (state==State.PLAYING) {
			pause();
			seek(getPosition());
			play();
		} else {
			seek(getPosition());
		}
	}
	
	/**
	 * Seek all video(s) to the specified time position
	 * @param position The time position in milliseconds
	 */
	@Override
	public void seek(long position) {
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.seek(position, TimeUnit.MILLISECONDS);
			}
		}
	}

	/**
	 * Stop all video(s)
	 */
	@Override
	public void stop() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.stop();
	}

	/**
	 * Iconify all video(s) windows
	 */
	public void iconify(boolean b) {
		for (Shell s : shells)
			if (s != null)
				s.setMinimized(b);
	}

}
