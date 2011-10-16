package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;
import static org.eclipse.swt.SWT.OPEN;

import java.util.ArrayList;
import java.util.List;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Slice;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.Track;
import it.polito.atlas.alea2.TrackLIS;
import it.polito.atlas.alea2.TrackText;
import it.polito.atlas.alea2.TrackVideo;
import it.polito.atlas.alea2.db.DBStorage;
import it.polito.atlas.alea2.initializer.TabFolderInitializer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class MainWindowShell {

	private static final Shell instance;
	private static final FormLayout layout;

	/**
	 * @return the layout
	 */
	public static FormLayout getLayout() {
		return layout;
	}

	private static boolean run;

	public static Shell shell() {
		return instance;
	}

	static {
		instance = new Shell(display());
		instance.setText("Alea2");
		layout = new FormLayout();
		instance.setLayout(layout);
	}

	/**
	 * Annotation Editor list
	 */
	public static List<AnnotationShell> annotationShell = new ArrayList<AnnotationShell>();

	/**
	 * Open Projects list
	 */
	private static List<Project> projects = new ArrayList<Project>();

	/**
	 * @return the Open Projects List
	 */
	public static List<Project> getProjects() {
		return projects;
	}

	/**
	 * @return the current Project
	 */
	public static Project getCurrentProject() {
		TabFolder tf = TabFolderInitializer.getTabFolder();
		
		if ((tf == null) || (tf.getItemCount() == 0))
			return null;
		if (MainWindowShell.getProjects().size() != tf.getItemCount())  {
			System.out.println("The number of TabItems and OpenProjects are differents");
			return null;
		}
		return MainWindowShell.getProjects().get(tf.getSelectionIndex());
	}

	/**
	 * @return the current Tree
	 */
	public static Tree getCurrentTree() {
		Project p = getCurrentProject();
		if (p == null)
			return null;
		TabFolder tf = TabFolderInitializer.getTabFolder();
		Tree t = (Tree) p.link;
		if (t != (Tree) tf.getItem(tf.getSelectionIndex()).getControl()) {
			System.out.println("Project link and current Tree mismatch");
		}
		return t;
	}

	/**
	 * @return the Storage
	 */
	public static Storage getStorage() {
		return storage;
	}

	/**
	 * Projects Storage
	 */
	private static Storage storage = new DBStorage();

	/**
	 * Create a new TabItem and load the Project.
	 * Returns false if Project is already loaded
	 * @param p Project to load
	 */
	public static boolean openProject(Project p) {
		if (projects.contains(p))
			return false;
		if (!projects.add(p))
			return false;
		
		TabFolder tabFolder = TabFolderInitializer.getTabFolder();
		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(p.getName());
		tabItem.setData(p);
		
		// create the tree		
		final Tree tree = createTree(tabFolder);
		tabItem.setControl(tree);
		addProjectData(tree, p);
		return true;
	}
	
    private static final Tree createTree(TabFolder tabFolder) {
		final Tree tree = new Tree(tabFolder, SWT.CHECK | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		// check/select listener
	    tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          String string = event.detail == SWT.CHECK ? "Checked/Unchecked" : "Selected";
	          System.out.println(event.item + " " + string);
	        }
	    });

	    // pop up menus
	    MenuItem item;
	    final Menu contextMenuTrack = new Menu(instance, SWT.POP_UP);
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
			    	Slice s = new Slice(0, 0);
			    	addSliceData(i, s, t.getSlices().size());					
					i.setExpanded(true);
					t.addSlice(s);
				}
			}	    	
	    });
	    final Menu contextMenuAnnotation = new Menu(instance, SWT.POP_UP);
	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track LIS");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					Annotation a;
					try {
						a = (Annotation) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (a == null) {
						System.out.println("No link betweek TreeItem and Annotation");
						return;
					}
			    	String newTrackName = "New LIS Track";
			    	TrackLIS t = new TrackLIS(newTrackName);
			    	addTrackData(i, t);					
					i.setExpanded(true);
					a.addTrackLIS(t);
				}
			}	    	
	    });
	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track Video");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String path=openVideoShell();
				if (path==null)
					return;
				for (TreeItem i : tree.getSelection()) {
					Annotation a;
					try {
						a = (Annotation) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (a == null) {
						System.out.println("No link betweek TreeItem and Annotation");
						return;
					}
			    	String newTrackName = path;
			    	TrackVideo t = new TrackVideo(newTrackName);
			    	addTrackData(i, t);					
					i.setExpanded(true);
					a.addTrackVideo(t);
				}
			}	    	
	    });
	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track Text");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					Annotation a;
					try {
						a = (Annotation) i.getData();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
					if (a == null) {
						System.out.println("No link betweek TreeItem and Annotation");
						return;
					}
			    	String newTrackName = "New Text Track";
			    	TrackText t = new TrackText(newTrackName);
			    	addTrackData(i, t);
					i.setExpanded(true);
					a.addTrackText(t);
				}
			}	    	
	    });
	    
	    final Menu contextMenuAddAnnotation = new Menu(instance, SWT.POP_UP);
	    item = new MenuItem(contextMenuAddAnnotation, SWT.PUSH);
	    item.setText("Add Annotation");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Project p = getCurrentProject();
				if (p == null)
					return;
		    	String newAnnotationName = "New Annotation";
		    	Annotation a = new Annotation(newAnnotationName);
				TreeItem aItem = addAnnotationData(tree, a);
				aItem.setExpanded(true);
				p.addAnnotation(a);
			}	    	
	    });
	    
		// mouse listener
		tree.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
	          Point point = new Point(event.x, event.y);
	          System.out.println("Point: " + point.x + "," + point.y);
	          TreeItem item = tree.getItem(point);
	          if (item != null) {
	        	  System.out.println("Mouse inside item");
	        	  for (TreeItem i : tree.getSelection()) {
	        		  Object o=i.getData();
	        		  if (o instanceof Annotation) {
	        			  tree.setMenu(contextMenuAnnotation);
	        		  } else if (o instanceof Track) {
	        			  tree.setMenu(contextMenuTrack);
	        		  } else {
	        			  tree.setMenu(null);
	        		  }
	        		  if (o != null)
	        			  System.out.println(i + ": " + o.getClass());
		          }
	          } else {
	        	  System.out.println("No Item");
		         // System.out.println("Rect: " + r.x + "," + r.y + "," + r.width + "," + r.height);
    			  tree.setMenu(contextMenuAddAnnotation);
	          }
	        }
	    });

	    // columns
		tree.setHeaderVisible(true);
	    TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
	    column1.setText("Name");
	    column1.setWidth(200);
	    column1.setMoveable(true);
	    TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
	    column2.setText("Type");
	    column2.setWidth(200);
	    column2.setMoveable(true);
	    TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
	    column3.setText("Info");
	    column3.setWidth(200);
	    column3.setMoveable(true);
		tree.pack();
		return tree;
	}

	public static void updateTree(final Tree tree, Project p) {
		tree.clearAll(true);
		addProjectData(tree, p);
	}

	/** fill tree with project data
     * 
     * @param p
     */
	public static void addProjectData(Tree tree, Project p) {
		p.link = tree;
	    for (Annotation a : p.getAnnotations()) {
	    	TreeItem aItem = addAnnotationData(tree, a);
	    	if (aItem != null)
	    		aItem.setExpanded(true);
		}
	}

	private static TreeItem addAnnotationData(Tree tree, Annotation a) {
		TreeItem aItem = new TreeItem(tree, SWT.NONE);
		aItem.setText(new String[] { a.getName(), "annotation", String.valueOf(a.getTracks().size()) + " tracks"});
		aItem.setData(a);
		for (Track t : a.getTracks()) {
			addTrackData(aItem, t);
		}
		aItem.setExpanded(true);
		return aItem;
	}

	private static TreeItem addTrackData(TreeItem aItem, Track t) {
    	TreeItem tItem = new TreeItem(aItem, SWT.NONE);
		tItem.setText(new String[] { t.getName(), t.getTypeString() + " track", String.valueOf(t.getSlices().size()) + " slices"});
		tItem.setData(t);
    	t.link = tItem;
		int i=0;
		for (Slice s : t.getSlices()) {
			addSliceData(tItem, s, i);
		}
		tItem.setExpanded(true);
		return tItem;
	}

	private static TreeItem addSliceData(TreeItem tItem, Slice s, int i) {
		++i;
		TreeItem sItem = new TreeItem(tItem, SWT.NONE);
		sItem.setText(new String[] { "slice " + i, "slice", "duration: " + s.getStartTime() + " - " + s.getEndTime()});
		sItem.setData(s);
		return sItem;
	}

	public static String openVideoShell() {
		FileDialog dialog = new FileDialog(shell(), OPEN);
		
		String[] filterNames = new String[] { "Video Files (*.avi, *.mov, *.mpg, *.mp4)", "All Files (*)"};
		String[] filterExtensions = new String[] { "*.avi; *.mov; *.mpg; *.mp4", "*" };

		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);

		String path = dialog.open();
		return path;
	}

	public static void runShell() {
		if (!run) {
			instance.setSize(640, 480);
			instance.setLocation(SWT.DEFAULT, SWT.DEFAULT);

			instance.open();

			while (!instance.isDisposed()) {
				if (!display().readAndDispatch()) {
					display().sleep();
				}
			}
			display().dispose();
		}
	}
}
