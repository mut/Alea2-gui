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
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swt.VideoComponent;

public class SWTPlayer implements it.polito.atlas.alea2.Player {
	
	public List <PlayBin2> videos = new ArrayList<PlayBin2>();
	
	public void addVideo(String file) {

		PlayBin2 playbin = new PlayBin2("VideoPlayer");
		
		final String title = "SWT Video #";

/*
		videos.addMany(playbin, playbin2);
		videos.seek(10, TimeUnit.SECONDS);
*/
		try {
			playbin.setInputFile(new File(file));
		} catch (Exception e) {
			System.out.print("Can't open file: " + file);
		}

		try {
			final Shell shell = new Shell(display());
			videos.add(playbin);
			shell.setText("SWT Video #" + videos.size() + " - " + file);

			shell.addShellListener(new ShellListener() {

				@Override
				public void shellIconified(ShellEvent e) {
					pause();
				}

				@Override
				public void shellDeiconified(ShellEvent e) {
					play();
				}

				@Override
				public void shellDeactivated(ShellEvent e) {
				}

				@Override
				public void shellClosed(ShellEvent e) {
					int indexVideo=Integer.parseInt(shell.getText().substring(title.length(), title.length()+1))-1;
					PlayBin2 p = videos.get(indexVideo);
					p.stop();
					p.dispose();
					videos.set(indexVideo, null);
				}

				@Override
				public void shellActivated(ShellEvent e) {
				}
			});
			
			shell.setSize(640, 480);
			shell.setLayout(new FillLayout());

			final VideoComponent component = new VideoComponent(shell, SWT.NONE);
			component.getElement().setName("Video #" + videos.size());
			component.setKeepAspect(true);
			// shell.setSize(component.getSize());
			// component.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			// true));//GridData.FILL_BOTH));

			playbin.setVideoSink(component.getElement());
			// shell.pack();
			shell.open();

			playbin.setState(State.PAUSED);

			// Element sink = component.getElement();

			// pipe.addMany(src, id, sink);
			// Element.linkMany(src, id, sink);
			// pipe.setState(State.PLAYING);

		} catch (Exception e) {
		}
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public long getEndTime() {
		// TODO Auto-generated method stub
		return 0;
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
		boolean first = true;
		if (resync) {
			long position=0;
			for (PlayBin2 p : videos) {
				if (p != null) {
					p.pause();
					if (first) {
						position=p.queryPosition(TimeUnit.MILLISECONDS);
					}
				}
			}
			seek(position);
		}
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.play();
			}
		}
	}

	@Override
	public void seek(long arg0) {
		for (PlayBin2 p : videos) {
			if (p != null) {
				p.seek(arg0, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public void stop() {
		for (PlayBin2 p : videos)
			if (p != null)
				p.stop();
	}

}
