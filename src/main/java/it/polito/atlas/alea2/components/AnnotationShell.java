package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;
import static org.eclipse.swt.SWT.PUSH;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Slice;
import it.polito.atlas.alea2.Track;
import it.polito.atlas.alea2.TrackVideo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class AnnotationShell {
	private Shell shell;
	private SWTPlayer player;
	private Annotation annotation;

	static final int TIME_OUT = 125;
	private boolean updatingScale;
	private long maxLength=0; 	
	private String maxLengthString="0"; 
	
	// common layout
	private Scale scale;
	
	// new layout
	Tree tree;
	Canvas panel;
	CoolBar playCommands;
		

	public Shell shell() {
		return shell;
	}

	/**
	 * Timer for updating actual time
	 */
	final Runnable updaterRunnable = new Runnable() {
        public void run() {
            MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);
			
			if (maxLength==0) {
				maxLength=player.getEndTime();
				maxLengthString = SWTPlayer.timeString(maxLength);
				updatingScale = true;
				scale.setMaximum((int) maxLength / 1000);
				updatingScale = false;
			}

			long pos = player.getPosition();
			shell().setText(annotation.getName() + " " + SWTPlayer.timeString(pos) + " / " + maxLengthString);

			if (!updatingScale) {
			  	updatingScale = true;
			  	scale.setSelection((int) pos / 1000);
			  	updatingScale = false;
			}
			panel.redraw();
			return;
      }
    };
	private CoolBar coolBar;
	protected boolean mouseDown=false;
	protected int xMouseDown=-1;
	protected int xMouse=-1;
	protected int yMouseDown;
	protected int yMouse;

    public AnnotationShell(Annotation a) {
		updatingScale = false;
		annotation = a;
		player = new SWTPlayer();
		
		for (TrackVideo t : a.getTracksVideo()) {
			System.out.println(t.getName());			
			player.addVideo(t.getName());
		}
		
		shell = new Shell(display());
		shell.setText("Annotation Editor");
		shell.setSize(640, 480);
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
				shell=null;
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setLayout();
		annotationUpdate();
        MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);
		shell().open();
	}
    
    private void setLayout() {
		shell().setLayout(new FormLayout());
		Point size;
		
		final Sash vSash = new Sash(shell(), SWT.BORDER | SWT.VERTICAL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0);
		formData.bottom = new FormAttachment(100);
		formData.left = new FormAttachment(30);
		vSash.setLayoutData(formData);	    
		vSash.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
		        ((FormData) vSash.getLayoutData()).left = new FormAttachment(0, event.x);
		        vSash.getParent().layout();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    
		// Scale
		scale = new Scale(shell(), SWT.BORDER);
		scale.pack();
		size = scale.getSize();
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(vSash);
		formData.right = new FormAttachment(100);
		//formData.top = new FormAttachment(label);
		formData.bottom = new FormAttachment(100);
		scale.setLayoutData(formData);
		// scale moving
		scale.addSelectionListener( new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (updatingScale)
					return;
				updatingScale = true;
				
				int perspectiveValue = scale.getSelection();
				//System.out.println(perspectiveValue);
		        player.seek(perspectiveValue*1000);
		        
		        updatingScale = false;
			}			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		// Player control Toolbar
		coolBar = new CoolBar(shell(), SWT.BORDER_SOLID);
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
		addTool(toolBar, "first.png", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.seek(0);
			}
		}, "Go Start");
		addTool(toolBar, "play.png", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.play(false);
			}
		}, "Play");
		addTool(toolBar, "pause.png", new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.pause();
			}
		}, "Pause");
		addTool(toolBar, "last.png", new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.seek(player.maxDuration);
			}
		}, "Go End");
		addSeparator(toolBar);
		addTool(toolBar, "resync.png", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				player.play(true);
			}
		}, "Play Resync");
		toolBar.pack();
		// Add a coolItem to a coolBar
		CoolItem coolItem = new CoolItem(coolBar, SWT.NULL);
		// set the control of the coolItem
		coolItem.setControl(toolBar);
		size = toolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT); 
		Point coolSize = coolItem.computeSize(size.x, size.y);
		coolItem.setSize(coolSize);
		coolBar.pack();
		size = coolBar.getSize();
		FormData coolData = new FormData(size.x, size.y);
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(vSash);
		//coolData.top= new FormAttachment(tree);
		coolData.bottom = new FormAttachment(100);
		coolBar.setLayoutData(coolData);

		// Tree
		tree = new Tree(shell(), SWT.H_SCROLL | SWT.V_SCROLL);
	    formData = new FormData();
	    formData.top = new FormAttachment(0);
	    formData.bottom = new FormAttachment(coolBar);
	    formData.left = new FormAttachment(0);
	    formData.right = new FormAttachment(vSash);
	    tree.setLayoutData(formData);
		
	    // Create a canvas
	    panel = new Canvas(shell(), SWT.NONE);

	    formData = new FormData();
	    formData.top = new FormAttachment(0);
	    formData.bottom = new FormAttachment(scale);
	    formData.left = new FormAttachment(vSash);
	    formData.right = new FormAttachment(100);
	    panel.setLayoutData(formData);
	    
	    panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				mouseDown=false;
				xMouse=arg0.x;
				
				long duration = player.getEndTime();
       			if (duration==0)
       				return;
	    		Rectangle clientArea = panel.getClientArea();
				if (clientArea.width==0)
					return;
				
				float x1 = xMouseDown*duration/clientArea.width;
				float x2 = xMouse*duration/clientArea.width;
				if (x1>x2) {
					float tmp=x1;
					x1=x2;
					x2=tmp;
				}
				
				for (TreeItem item : tree.getSelection()) {
					Object o = item.getData();
					if ((o != null) && (o instanceof Slice))  {
						((Slice) o).setStartTime((long)x1);
						((Slice) o).setEndTime((long)x2);
					}
				}
				panel.redraw();
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				if ((arg0.stateMask & SWT.BUTTON1) != 0)
					return;
				mouseDown=true;
				xMouseDown=arg0.x;
				xMouse=xMouseDown;
				yMouseDown=arg0.y;
				yMouse=yMouseDown;

				long duration = player.getEndTime();
       			if (duration==0)
       				return;
	    		Rectangle clientArea = panel.getClientArea();
				if (clientArea.width==0)
					return;
				
				selectItem();

				player.seek(xMouseDown*player.maxDuration/clientArea.width);
				panel.redraw();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
	    panel.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent arg0) {
				if (mouseDown) {
	       			long duration = player.getEndTime();
	       			if (duration==0)
	       				return;
		    		Rectangle clientArea = panel.getClientArea();
					if (clientArea.width==0)
						return;
					
					xMouse=arg0.x;
					player.seek(xMouse*player.maxDuration/clientArea.width);
					panel.redraw();
				}				
			}
		});

	    // Create a paint handler for the canvas
	    panel.addPaintListener(new PaintListener() {
	    	public void paintControl(PaintEvent e) {
	    		Device dev = e.gc.getDevice();
	    		Rectangle clientArea = panel.getClientArea();
                Rectangle trackRect;
                long duration=player.getEndTime();

	            //e.gc.drawFocus(rect.x, rect.y, rect.width - 10, rect.height - 10);
	    		//e.gc.drawText("text", 60, 60);
                e.gc.setBackground(dev.getSystemColor(SWT.COLOR_WHITE));
                e.gc.fillRectangle(clientArea);
  
                // mouse selection
                if (mouseDown) {
                	int x1=xMouseDown, x2=xMouse;
                	if (xMouse<xMouseDown) {
                		x1=xMouse; x2=xMouseDown;
                	}
    	    		e.gc.setBackground(dev.getSystemColor(SWT.COLOR_GRAY));
               		e.gc.fillRectangle(x1, clientArea.y, x2-x1, clientArea.height-1);
    	    		e.gc.setForeground(dev.getSystemColor(SWT.COLOR_RED));
               		e.gc.drawLine(xMouse, clientArea.y, xMouse, clientArea.height-1);
                } else {
    	    		e.gc.setForeground(dev.getSystemColor(SWT.COLOR_RED));
               		if (duration!=0) {
               			long x = player.getPosition() * clientArea.width / duration;
               			e.gc.drawLine((int) x, clientArea.y, (int) x, clientArea.height-1);
               		}
                }

                // tracks
                for (TreeItem i : tree.getItems()) {
                	trackRect = i.getBounds();
    	    		e.gc.setForeground(dev.getSystemColor(SWT.COLOR_BLACK));
               		e.gc.drawRectangle(0, trackRect.y, clientArea.width, trackRect.height-1);
               		for (TreeItem j : i.getItems()) {
                    	trackRect = j.getBounds();
        	    		e.gc.setForeground(dev.getSystemColor(SWT.COLOR_GREEN));
                   		e.gc.drawRectangle(0, trackRect.y, clientArea.width, trackRect.height-1);
                   		Slice s = j.getData() instanceof Slice?(Slice) j.getData():null;
                   		if (s != null) {
                   			if (duration==0)
                   				return;
                   			long start = s.getStartTime();
                   			long width = s.getEndTime()-start;
                   			if (width!=0) {
                   				float x1=start*clientArea.width/duration;
                   				float x2=width*clientArea.width/duration;
                	    		e.gc.setForeground(dev.getSystemColor(SWT.COLOR_BLUE));
                           		e.gc.drawRectangle((int) x1, trackRect.y+1, (int) x2, trackRect.height-3);
                	    		e.gc.setBackground(dev.getSystemColor(SWT.COLOR_CYAN));
                           		e.gc.fillRectangle((int) x1+1, trackRect.y+2, (int) x2-2, trackRect.height-5);
                   			}
                   		}
               		}
                }
	    	}
	    });
	}


	/**
	 * Select the item at the same mouse height 
	 */
	protected void selectItem() {
		for (TreeItem i : tree.getItems()) {
       		Rectangle rect=i.getBounds();
       		if ((rect.y <= yMouseDown) && (yMouseDown <= rect.y+rect.height)) {
       			tree.select(i);
       			return;
       		}
			for (TreeItem j : i.getItems()) {
           		rect=j.getBounds();
           		if ((rect.y <= yMouseDown) && (yMouseDown <= rect.y+rect.height)) {
           			tree.select(j);
           			return;
           		}
			}
		}
	}

	public String getProjectName() {
		return null;
	}
	
	/**
	 * Update the current shell by information inside Annotation
	 */
	public void annotationUpdate() {
		tree.removeAll();
		for (Track t : annotation.getTracks()) {
			addTrackData(tree, t);
		}
		
	}
	
	private static TreeItem addTrackData(Tree tree, Track t) {
    	TreeItem tItem = new TreeItem(tree, SWT.NONE);
		tItem.setText(t.getName()); //new String[] { t.getName(), t.getTypeString() + " track", String.valueOf(t.getSlices().size()) + " slices"});
		tItem.setData(t);
    	//t.link = tItem;
		int i=0;
		for (Slice s : t.getSlices()) {
			addSliceData(tItem, s, ++i);
		}
		tItem.setExpanded(true);
		return tItem;
	}

	private static TreeItem addSliceData(TreeItem tItem, Slice s, int i) {
		TreeItem sItem = new TreeItem(tItem, SWT.NONE);
		sItem.setText(new String[] { "slice " + i, "slice", "duration: " + s.getStartTime() + " - " + s.getEndTime()});
		sItem.setData(s);
		return sItem;
	}

	private void addTool(ToolBar toolBar, String icon, SelectionAdapter adapter, String toolTip) {
		ToolItem toolItem = new ToolItem(toolBar, PUSH);
		try {
			toolItem.setImage(new Image(shell().getDisplay(), getClass().getClassLoader().getResourceAsStream(icon)));
			toolItem.setToolTipText(toolTip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (adapter != null) {
			toolItem.addSelectionListener(adapter);
		}
	}
	
	private void addSeparator(ToolBar toolBar) {
		new ToolItem(toolBar, SWT.SEPARATOR);
	}

	public void dispose() {
	}
}
