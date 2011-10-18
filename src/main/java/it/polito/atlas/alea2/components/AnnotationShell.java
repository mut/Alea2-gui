package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import java.util.concurrent.TimeUnit;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Track;
import it.polito.atlas.alea2.TrackVideo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class AnnotationShell {
	private Shell shell;
	private SWTPlayer player;
	private Scale scale;
	private Annotation annotation;
	static final int TIME_OUT = 125;
	private boolean updatingScale;
	private long maxLength=0; 	
	private String maxLengthString="0"; 	

	public Shell shell() {
		return shell;
	}

	/**
	 * Timer for updating actual time
	 */
	final Runnable updaterRunnable = new Runnable() {
        public void run() {
            MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);
			if (updatingScale)
				return;
			
			if (maxLength==0) {
				maxLength=player.getEndTime();
				maxLengthString = SWTPlayer.timeString(maxLength);
				updatingScale = true;
				scale.setMaximum((int) maxLength / 1000);
				updatingScale = false;
			}

			long pos = player.getPosition();
			shell().setText(annotation.getName() + " " + SWTPlayer.timeString(pos) + " / " + maxLengthString);
		  	updatingScale = true;
		  	scale.setSelection((int) pos / 1000);
		  	updatingScale = false;
			return;
      }
    };

    public AnnotationShell(Annotation a) {
		updatingScale = false;
		annotation = a;
		player = new SWTPlayer();
        MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);


		
		for (TrackVideo t : a.getTracksVideo()) {
			System.out.println(t.getName());			
			player.addVideo(t.getName());
		}
		
		shell = new Shell(display());
		shell.setText("Annotation Editor");
		shell.setSize(640, 240);
		shell.setLocation(SWT.DEFAULT, SWT.DEFAULT);
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent arg0) {
				if (player == null)
					return;
				player.pause();
				player.iconify(true);
			}
			
			@Override
			public void shellDeiconified(ShellEvent arg0) {
				if (player == null)
					return;
				player.iconify(false);
			}
			
			@Override
			public void shellDeactivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellClosed(ShellEvent arg0) {
				MainWindowShell.getDiplay().timerExec(-1, updaterRunnable);
				player.dispose();
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		shell.setLayout(new FormLayout());
		
		Point size;
		FormData formData;
		
		// Labels
		Label label=null;
		Label oldLabel=null;
		for (Track t : annotation.getTracks()) {
			oldLabel=label;
			label = new Label(shell(), SWT.BORDER);
			label.setText("Track: " + t.getTypeString() + " " + t.getName());
			label.pack();
			size = label.getSize();
			formData = new FormData(size.x, size.y);
			formData.left = new FormAttachment(0);
			formData.right = new FormAttachment(100);
			if (oldLabel==null) {
				formData.top = new FormAttachment(0);
			} else {
				formData.top = new FormAttachment(oldLabel);
			}
			label.setLayoutData(formData);
		}
		
		// Buttons size
		size = label.getSize();
		size.y = 50;

		// Button Play
		Button ok = new Button(shell(), SWT.PUSH);
        ok.setText("Play / Resync");
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(50);
		formData.right = new FormAttachment(100);
		//formData.top = new FormAttachment(scale);
		formData.bottom = new FormAttachment(100);
		ok.setLayoutData(formData);
		ok.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.play();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Button Pause
		Button cancel = new Button(shell(), SWT.PUSH);
		cancel.setText("Pause");
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(50);
		//formData.top = new FormAttachment(scale);
		formData.bottom = new FormAttachment(100);
		cancel.setLayoutData(formData);
		cancel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.pause();
				System.out.println(player.videos.get(0).queryDuration(TimeUnit.MILLISECONDS));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});		

		// Scale
		scale = new Scale(shell(), SWT.BORDER);
		scale.pack();
		long max=player.getEndTime();
		scale.setMaximum ((int) max);
		System.out.println(max);
		
		size = scale.getSize();
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		//formData.top = new FormAttachment(label);
		formData.bottom = new FormAttachment(ok);
		scale.setLayoutData(formData);
		scale.addSelectionListener( new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (updatingScale)
					return;
				updatingScale = true;
				
				int perspectiveValue = scale.getSelection();
				System.out.println(perspectiveValue);
		        player.seek(perspectiveValue*1000);
		        
		        updatingScale = false;
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		shell.open();
	}
	public String getProjectName() {
		return null;
	}
	
	public void dispose() {
		shell().dispose();
	}
}
