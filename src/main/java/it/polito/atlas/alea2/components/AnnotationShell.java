package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;
import static org.eclipse.swt.SWT.PUSH;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Player;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Slice;
import it.polito.atlas.alea2.Track;
import it.polito.atlas.alea2.TrackLIS;
import it.polito.atlas.alea2.TrackText;
import it.polito.atlas.alea2.TrackVideo;
import it.polito.atlas.alea2.tule.TuleClient;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author  DANGELOA
 */
public class AnnotationShell {
	private Shell shell;
	/**
	 * @uml.associationEnd  
	 */
	private SWTPlayer player;
	private Annotation annotation;
	private Project project;
	public AnnotationShell instance() {
		return this;		
	}

	static final String name="Annotation Editor";
	
	/**
	 * The Background Worker call back frequency in milliseconds
	 */
	static final int TIME_OUT = 125;
	/**
	 * Defines scale granularity in millisecond
	 */
	static final int TIME_LINE_DENSITY = 1;
	/**
	 * Defines scale steps
	 */
	static final int TIME_LINE_STEP = 10;
	/**
	 * Set when update the scale (avoid concurrency)
	 */
	private boolean updatingScale;
	/**
	 * Maximum length of videos in milliseconds
	 */
	private long maxLength=0; 	
	private String maxLengthString="0"; 
	
	// Layout
	Tree tree;
	CoolBar playCommands;
	Canvas panel;
	private Scale scale;
		
	/**
	 * return the instance of the Annotation Shell
	 * @return
	 */
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
				scale.setMaximum((int) maxLength / TIME_LINE_DENSITY);
				updatingScale = false;
			}

			long pos = player.getPosition();
			shell().setText(name + " \"" + annotation.getName() + "\" [" + SWTPlayer.timeString(pos) + " / " + maxLengthString + "]");

			if (!updatingScale) {
			  	updatingScale = true;
			  	scale.setSelection((int) pos / TIME_LINE_DENSITY);
			  	updatingScale = false;
			}
			panel.redraw();
			return;
      }
    };
	private CoolBar coolBar;
	/**
	 * Mouse is clicked
	 */
	protected boolean mouseDown=false;
	/**
	 * Mouse x position when clicked
	 */
	protected int xMouseDown=-1;
	/**
	 * Current mouse x position
	 */
	protected int xMouse=-1;
	/**
	 * Mouse y position when clicked
	 */
	protected int yMouseDown;
	/**
	 * Current mouse x position
	 */
	protected int yMouse;

	/**
	 * @return the Player instance
	 */
	public Player getPlayer() {
		return player;
	};

    public AnnotationShell(Annotation a) {
    	try {
        	project = null;
        	annotation = a;
	
			updatingScale = false;
			player = new SWTPlayer();
			
			for (TrackVideo t : annotation.getTracksVideo()) {
				System.out.println(t.getName());			
				player.addVideo(t);
			}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		return;
    	}
    	buildShell();
		updateTree();
        MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);
		shell().open();
    }
    
    public AnnotationShell(Tree mainWindowTree) {
    	try {
    		project = (Project) mainWindowTree.getData();
	    	annotation = project.getCurrentAnnotation();
	
			updatingScale = false;
			player = new SWTPlayer();
			
			for (TrackVideo t : annotation.getTracksVideo()) {
				System.out.println(t.getName());			
				player.addVideo(t);
			}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		return;
    	}
		
    	buildShell();
		updateTree();
        MainWindowShell.getDiplay().timerExec(TIME_OUT, updaterRunnable);
		shell().open();
	}
    
    private void buildShell() {
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
				MainWindowShell.updateTree(getProjectTree(), project);
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
		        player.seek(perspectiveValue*TIME_LINE_DENSITY);
		        
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
				player.play();
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
				player.resync();
			}
		}, "Resync");
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
	    // Tree pop up menus
	    MenuItem item;
	    // Slice context menu
	    final Menu contextMenuSlice = new Menu(shell(), SWT.POP_UP);
	    item = new MenuItem(contextMenuSlice, SWT.PUSH);
	    item.setText("Remove Slice");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				for (TreeItem i : tree.getSelection()) {
					Slice s;
					try {
						s = (Slice) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (s == null) {
						System.out.println("No link betweek TreeItem and Slice");
						return;
					}
			    	s.remove();
			    	updateTree();
				}
			}
		});

	    // Track context menu
	    final Menu contextMenuTrack = new Menu(shell(), SWT.POP_UP);
	    item = new MenuItem(contextMenuTrack, SWT.PUSH);
	    item.setText("Add Slice");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//MessageBox mb = new MessageBox(instance);
				//mb.setMessage("Insert the name: ");
				//mb.open();
				for (TreeItem i : tree.getSelection()) {
					Track t;
					try {
						t = (Track) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (t == null) {
						System.out.println("No link betweek TreeItem and Track");
						return;
					}
			    	Slice s = new Slice(t, 0, 0);
			    	t.addSlice(s);
			    	updateTree();
				}
			}	    	
	    });
	    item = new MenuItem(contextMenuTrack, SWT.PUSH);
	    item.setText("Remove Track");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				for (TreeItem i : tree.getSelection()) {
					Track t;
					try {
						t = (Track) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (t == null) {
						System.out.println("No link betweek TreeItem and Track");
						return;
					}
			    	t.remove();
			    	updateTree();
				}
			}
		});

	    // Track Text context menu
	    final Menu contextMenuTrackText = new Menu(shell(), SWT.POP_UP);
	    item = new MenuItem(contextMenuTrackText, SWT.PUSH);
	    item.setText("Add Lemmas");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					TrackText t;
					try {
						t = (TrackText) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (t == null) {
						System.out.println("No link betweek TreeItem and Track");
						return;
					}
			    	t.addLemmas(t.getName());
				}
				updateTree();
			}	    	
	    });
	    item = new MenuItem(contextMenuTrackText, SWT.PUSH);
	    item.setText("Extract Lemmas throught Tule");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					TrackText t;
					try {
						t = (TrackText) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (t == null) {
						System.out.println("No link betweek TreeItem and Track");
						return;
					}
			    	TuleClient tule = new TuleClient();
			    	t.addTuleLemmas(t.getName(), tule);
				}
				updateTree();
			}	    	
	    });
	    item = new MenuItem(contextMenuTrackText, SWT.PUSH);
	    item.setText("Remove Track");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				for (TreeItem i : tree.getSelection()) {
					Track t;
					try {
						t = (Track) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (t == null) {
						System.out.println("No link betweek TreeItem and Track");
						return;
					}
			    	t.remove();
			    	updateTree();
				}
			}
		});

	    // Annotation context menu
	    final Menu contextMenuAnnotation = new Menu(shell(), SWT.POP_UP);
	    final Menu lisMenu = new Menu(shell(), SWT.DROP_DOWN);
	    item = new MenuItem(contextMenuAnnotation, SWT.CASCADE);
	    item.setText("Add Track LIS");
	    item.setMenu(lisMenu);
	    addLISMenu(shell(), lisMenu, getLisListener());

	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track Video");
	    item.addListener(SWT.Selection, getVideoListener());
	    
	    final Menu textMenu = new Menu(shell(), SWT.DROP_DOWN);
	    item = new MenuItem(contextMenuAnnotation, SWT.CASCADE);
	    item.setText("Add Track Text");
	    item.setMenu(textMenu);
	    addTextMenu(shell(), textMenu);
	    
		// Mouse listener: assign the right context menu by the item type
	    tree.addListener(SWT.MouseDown, new Listener() {

	    	public void handleEvent(Event event) {
	    		Point point = new Point(event.x, event.y);
	    		// System.out.println("Point: " + point.x + "," + point.y);
	    		TreeItem item = tree.getItem(point);
	    		if (item != null) {
	    			// System.out.println("Mouse inside item");
	    			for (TreeItem i : tree.getSelection()) {
	    				Object o=i.getData();
	    				if (o instanceof Annotation) {
	    					tree.setMenu(contextMenuAnnotation);
	    				} else if (o instanceof TrackText) {
	    					tree.setMenu(contextMenuTrackText);
	    				} else if (o instanceof Track) {
	    					tree.setMenu(contextMenuTrack);
	    				} else if (o instanceof Slice) {
	    					tree.setMenu(contextMenuSlice);
	    				} else {
	    	    			System.out.println("Unknown item type: no context menu");
	    					tree.setMenu(null);
	    				}
	    				if (o != null)
	    					System.out.println(i + ": " + o.getClass());
	    			}
	    		} else {
	    			System.out.println("No Item");
		         	// System.out.println("Rect: " + r.x + "," + r.y + "," + r.width + "," + r.height);
		         	tree.setMenu(contextMenuAnnotation);
	    		}
	    	}
	    });
		
	    // Create the panel with Tracks
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
				if (arg0.button != 1)
					return;
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
				updateTree();
				panel.redraw();
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				System.out.println(arg0.button + " - " + arg0.stateMask);
				//if ((arg0.stateMask & SWT.BUTTON1) != 0)
				if (arg0.button != 1)
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
				xMouse=arg0.x;
				if (mouseDown) {
	       			long duration = player.getEndTime();
	       			if (duration==0)
	       				return;
		    		Rectangle clientArea = panel.getClientArea();
					if (clientArea.width==0)
						return;
					
					player.seek(xMouse*player.maxDuration/clientArea.width);
					panel.redraw();
				}				
			}
		});

	    // Create a paint handler for the panel
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
  
                // Paint the mouse selection
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

                // Paint the tracks and slices
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


	private void addLISMenu(Shell shell2, Menu lisMenu, Listener lisListener) {
		MainWindowShell.addLISMenu(shell(), lisMenu, getLisListener());
	}

	Listener getVideoListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (tree  == null)
					return;
				String path=MainWindowShell.openVideoShell(shell());
				if (path  == null)
					return;
		    	TrackVideo t = new TrackVideo(annotation, path);
				annotation.addTrackVideo(t);
				updateTree();
				player.open(t);
			}
		};
	}
	
	void addTextMenu(Shell instance, Menu textMenu) {
        MenuItem item;
	    item = new MenuItem(textMenu, SWT.CASCADE);
	    item.setText("Open from file...");
	    item.addListener(SWT.Selection, getTextListener());
	    item = new MenuItem(textMenu, SWT.CASCADE);
	    item.setText("Insert a text...");
	    item.addListener(SWT.Selection, createTextShellListener());
	}

	Listener getTextListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (tree  == null)
					return;
				String path=MainWindowShell.openTextShell(shell());
		    	String text=MainWindowShell.getTextFile(path);
		    	if (text==null)
		    		return;
				annotation.addTextTrack(text);
				updateTree();
			}	    	
	    };
	}
	
	Listener createTextShellListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (tree  == null)
					return;
				new CreateTextShell(instance(), annotation);
				updateTree();
			}	    	
	    };
	}
	
	private Listener getLisListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				MenuItem item = (MenuItem) event.widget;
		        String newTrackName = item.getText();
		    	TrackLIS t = new TrackLIS(annotation, newTrackName);
				annotation.addTrackLIS(t);
				updateTree();
			}	    	
	    };
	}

	/**
	 * Select the item at the same mouse y position 
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

	public Tree getProjectTree() {
		if (project == null)
			return null;
		return (Tree) project.link;
	}
	
	/**
	 * Update the current shell by information inside the attached Annotation
	 */
	protected void updateTree() {
		tree.removeAll();
		for (Track t : annotation.getTracks()) {
			addTrackData(tree, t);
		}
	}

	/**
	 * Insert a Track inside the Tree
	 * @param tree The Tree
	 * @param t The Track
	 * @return
	 */
	private static TreeItem addTrackData(Tree tree, Track t) {
    	TreeItem tItem = new TreeItem(tree, SWT.NONE);
		tItem.setText(t.getName()); //new String[] { t.getName(), t.getTypeString() + " track", String.valueOf(t.getSlices().size()) + " slices"});
		tItem.setData(t);
    	//t.link = tItem;
		int i=0;
		for (Slice s : t.getSlices()) {
			MainWindowShell.addSliceData(tItem, s, ++i, false);
		}
		tItem.setExpanded(true);
		return tItem;
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
