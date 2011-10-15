package it.polito.atlas.alea2.adapters;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.components.MainWindowStatusBar.getStatusBar;
import static org.eclipse.swt.SWT.OPEN;

import it.polito.atlas.alea2.components.SWTPlayer;

//import java.util.concurrent.TimeUnit;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

public class OpenVideoAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		FileDialog dialog = new FileDialog(shell(), OPEN);

		String[] filterNames = new String[] { "Video Files (*.avi, *.mov, *.mpg, *.mp4)", "All Files (*)"};

		String[] filterExtensions = new String[] { "*.avi; *.mov; *.mpg; *.mp4", "*" };

		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);

		String path = dialog.open();
		if (path == null) {
			return;
		}
		getStatusBar().setText(path);
		getStatusBar().pack();
		
		SWTPlayer pl = new SWTPlayer();
		pl.addVideo(path);
		pl.play();
	}

	/*
	 * public static void play(String file) { String args[] = {"",""}; args =
	 * Gst.init("Alea2", args); final PlayBin playbin = new
	 * PlayBin("VideoPlayer"); try { playbin.setInputFile(new File(file)); }
	 * catch (Exception e) { playbin.setInputFile(new
	 * File("E:\\aMule\\Incoming\\D&D - The Gamers - sub ITA.avi")); }
	 * 
	 * SwingUtilities.invokeLater(new Runnable() {
	 * 
	 * public void run() { VideoComponent videoComponent = new VideoComponent();
	 * playbin.setVideoSink(videoComponent.getElement());
	 * 
	 * JFrame frame = new JFrame("VideoPlayer");
	 * frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
	 * frame.setPreferredSize(new Dimension(640, 480));
	 * frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	 * frame.pack(); frame.setVisible(true); playbin.setState(State.PLAYING); }
	 * }); Gst.main(); playbin.setState(State.NULL); }
	 */

}
