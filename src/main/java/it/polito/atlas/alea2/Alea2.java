package it.polito.atlas.alea2;

import org.eclipse.swt.SWT;
/*
 * import org.eclipse.swt.events.ControlEvent;
 * import org.eclipse.swt.events.ControlListener;
 * import org.eclipse.swt.events.DisposeEvent;
 * import org.eclipse.swt.events.DisposeListener;
*/
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;

//import java.awt.BorderLayout;
//import java.awt.Dimension;
import it.polito.atlas.alea2.db.DBStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import javax.swing.*;

import org.gstreamer.*;
import org.gstreamer.elements.*;
import org.gstreamer.swt.VideoComponent;

public class Alea2 {
    private static Display display;
	private static Shell shell, shell2;
	private Label status;
    private MenuItem statItem;
    private Project p = new Project("test");

    public Alea2() {

	        shell = new Shell(display);
	        shell.setText("Hello");

	        initUI();

	        shell.setSize(250, 200);
	        shell.setLocation(300, 300);

	        shell.open();
	        shell.setToolTipText(p.getName());

	        while (!shell.isDisposed()) {
	          if (!display.readAndDispatch()) {
	            display.sleep();
	          }
	        }
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (com.sun.jna.Platform.isMac()) {
			   final String jnaLibraryPath = System.getProperty("jna.library.path");
			   final StringBuilder newJnaLibraryPath = new StringBuilder(jnaLibraryPath != null ? (jnaLibraryPath + ":") : "");
			   newJnaLibraryPath.append("/System/Library/Frameworks/GStreamer.framework/Versions/0.10-" + (com.sun.jna.Platform.is64Bit() ? "x64" : "i386") + "/lib:");
			   System.setProperty("jna.library.path", newJnaLibraryPath.toString());
		} 
		display = new Display();
        new Alea2();
        display.dispose();
	}
	
    private void initUI() {
        initUIMenu();
        initUIToolBar();
    	initUIStatusBar();
    	initUIPopUp(shell);
    	
        Button quit = new Button(shell, SWT.PUSH);
        quit.setText("Quit");
        quit.setBounds(50, 50, 80, 30);
        
        quit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
    }
    
    private void initUIToolBar() {
		Image newi = null;
		Image opei = null;
		Image quii = null;
		
		Device dev = shell.getDisplay();

        try {
            newi = new Image(dev, "new.png");
            opei = new Image(dev, "open.png");
            quii = new Image(dev, "quit.png");

        } catch (Exception e) {
            System.out.println("Cannot load images");
            System.out.println(e.getMessage());
            //System.exit(1);
        }

        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);

        ToolItem item1 = new ToolItem(toolBar, SWT.PUSH);
        item1.setImage(newi);

        ToolItem item2 = new ToolItem(toolBar, SWT.PUSH);
        item2.setImage(opei);

        new ToolItem(toolBar, SWT.SEPARATOR);

        ToolItem item3 = new ToolItem(toolBar, SWT.PUSH);
        item3.setImage(quii);

        toolBar.pack();

        item3.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
	}

	private void initUIMenu() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        MenuItem subMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
        subMenuItem.setText("Import");

        Menu submenu = new Menu(shell, SWT.DROP_DOWN);
        subMenuItem.setMenu(submenu);

        MenuItem openProjectItem = new MenuItem(submenu, SWT.PUSH);
        openProjectItem.setText("&open project...");
        openProjectItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<Project> projects = new ArrayList<Project> ();
				Storage st = new DBStorage();
				for (String s : st.getProjectNamesList()) {
					System.out.println(s);
					projects.add(st.readProject(s));
				}
			}
		});

        MenuItem openItem = new MenuItem(submenu, SWT.PUSH);
        openItem.setText("&open file...");

        openItem.addSelectionListener(new SelectionAdapter() {
	    	
	        @Override
	        public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);

                String[] filterNames = new String[] 
                    {"All Files (*)", "Java sources"};

                String[] filterExtensions = new String[] 
                    {"*", "*.java"};

                dialog.setFilterNames(filterNames);
                dialog.setFilterExtensions(filterExtensions);

                String path = dialog.open();
                if (path != null) {
                	status.setText(path);
                	status.pack();
                }
                //play(path);
                playSWT(path);
	        }
	
	    });
	
        MenuItem bmarks = new MenuItem(submenu, SWT.PUSH);
        bmarks.setText("&Import bookmarks...");

        MenuItem mailItem = new MenuItem(submenu, SWT.PUSH);
        mailItem.setText("&Import mail...");

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");
        shell.setMenuBar(menuBar);

       exitItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
	
        MenuItem cascadeViewMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeViewMenu.setText("&View");

        Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeViewMenu.setMenu(viewMenu);

        statItem = new MenuItem(viewMenu, SWT.CHECK);
        statItem.setSelection(true);
        statItem.setText("&View Statusbar");

        statItem.addListener(SWT.Selection, statListener);

    }

    Listener statListener = new Listener() {
        public void handleEvent(Event event) {
            if (statItem.getSelection()) {
                status.setVisible(true);
            } else {
                status.setVisible(false);
            }
        }
    };
    
    void initUIStatusBar() {
        status = new Label(shell, SWT.BORDER);
        status.setText("Ready");
        FormLayout layout = new FormLayout();
        shell.setLayout(layout);

        FormData labelData = new FormData();
        labelData.left = new FormAttachment(0);
        labelData.right = new FormAttachment(100);
        labelData.bottom = new FormAttachment(100);
        status.setLayoutData(labelData);
    }
    
    void initUIPopUp(Decorations d) {
        Menu menu = new Menu(d, SWT.POP_UP);

        MenuItem minItem = new MenuItem(menu, SWT.PUSH);
        minItem.setText("Minimize");

        minItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.setMinimized(true);
            }
        });


        MenuItem exitItem = new MenuItem(menu, SWT.PUSH);
        exitItem.setText("Exit");

        exitItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });

        shell.setMenu(menu);
    }

    /*public static void play(String file) {
        String args[] = {"",""};
        args = Gst.init("Alea2", args);
        final PlayBin playbin = new PlayBin("VideoPlayer");
        try {
            playbin.setInputFile(new File(file));
        } catch (Exception e) {
            playbin.setInputFile(new File("E:\\aMule\\Incoming\\D&D - The Gamers - sub ITA.avi"));
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                VideoComponent videoComponent = new VideoComponent();
                playbin.setVideoSink(videoComponent.getElement());

                JFrame frame = new JFrame("VideoPlayer");
                frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
                frame.setPreferredSize(new Dimension(640, 480));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                playbin.setState(State.PLAYING);
            }
        });
        Gst.main();
        playbin.setState(State.NULL);
    }
    */
    public static void playSWT(String file) {
        String args[] = {"",""};
        args = Gst.init("Alea2", args);

        Pipeline videos = new Pipeline();
        final PlayBin2 playbin = new PlayBin2("VideoPlayer");
        final PlayBin2 playbin2 = new PlayBin2("VideoPlayer");
        videos.addMany(playbin, playbin2);
        videos.seek(10, TimeUnit.SECONDS);
        
        try {
            playbin.setInputFile(new File(file));
            playbin2.setInputFile(new File(file));
        } catch (Exception e) {
            playbin.setInputFile(new File("D:\\a.avi"));
            playbin2.setInputFile(new File("D:\\a.avi"));
        }
        
        try {
                shell2 = new Shell(display);
                shell2.addShellListener(new ShellListener() {
					
					@Override
					public void shellIconified(ShellEvent e) {
						playbin.pause();						
					}
					
					@Override
					public void shellDeiconified(ShellEvent e) {
						playbin.play();						
					}
					
					@Override
					public void shellDeactivated(ShellEvent e) {
					}
					
					@Override
					public void shellClosed(ShellEvent e) {
						playbin.stop();
						playbin.dispose();						
					}
					
					@Override
					public void shellActivated(ShellEvent e) {
					}
				});
                shell2.setSize(640, 480);
                shell2.setLayout(new FillLayout());
                shell2.setText("SWT Video Test - " + file);
                
                final VideoComponent component = new VideoComponent(shell2, SWT.NONE);
                component.getElement().setName("video");
                component.setKeepAspect(true);
                //shell2.setSize(component.getSize());
                //component.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));//GridData.FILL_BOTH));

                playbin.setVideoSink(component.getElement());
                //shell2.pack();
                shell2.open();

                videos.setState(State.PLAYING);

                //Element sink = component.getElement();


               // pipe.addMany(src, id, sink);
               // Element.linkMany(src, id, sink);
               // pipe.setState(State.PLAYING);
                
                //while (!shell2.isDisposed()) {
                //        if (!display.readAndDispatch())
                //                display.sleep();
                //}
//                display.dispose();

        } catch (Exception e) {
        }
}

}
