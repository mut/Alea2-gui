package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swt.VideoComponent;

public class SWTPlayer implements it.polito.atlas.alea2.Player {
	
	public List <PlayBin2> videos = new ArrayList<PlayBin2>();
	public List <Shell> shells = new ArrayList<Shell>();
	long maxDuration=0;
	
	public void addVideo(String file) {
		PlayBin2 playbin;
		Shell shell;

		final String title = "SWT Video #";
		
		try {
			
			playbin = new PlayBin2("VideoPlayer");
			shell = new Shell(display());
			try {
				playbin.setInputFile(new File(file));
			} catch (Exception e) {
				System.out.print("Can't open file: " + file);
			}

			final int index = shells.size();
			
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

			shell.open();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		shells.add(shell);
		videos.add(playbin);
	}
	
	@Override
	public void dispose() {
		for (Shell s : shells)
			if (s != null)
				s.close();
	}

	@Override
	public long getEndTime() {
		long duration;
		for (PlayBin2 p : videos)
			if (p != null) {
				duration = p.queryDuration(TimeUnit.MILLISECONDS);
				System.out.println("duration  = " + duration + "ms");
				if (duration>maxDuration)
					maxDuration=duration;
			}
		return maxDuration;
	}

	@Override
	public long getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.pause();
	}

	@Override
	public void play() {
		play(true);
	}
	
	public void play(boolean resync) {
		if (resync) {
			for (PlayBin2 p : videos) {
				if (p != null) {
					p.pause();
				}
			}

			for (PlayBin2 p : videos) {
				if (p != null) {
					p.pause();
				}
			}

			long position=0;
			for (PlayBin2 p : videos) {
				if (p != null) {
					position=p.queryPosition(TimeUnit.MILLISECONDS);
					System.out.println("Pre:  " + position);
					break;
				}
			}
			seek(position);
			for (PlayBin2 p : videos) {
				if (p != null) {
					System.out.println("Post: " + p.queryPosition(TimeUnit.MILLISECONDS));
				}
			}
		}
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.play();
			}
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
