package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import java.util.ArrayList;
import java.util.List;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Slice;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.Track;
import it.polito.atlas.alea2.db.DBStorage;
import it.polito.atlas.alea2.initializer.TabFolderInitializer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
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
		TabFolder tf = TabFolderInitializer.getTabFolder();
		
		if ((tf == null) || (tf.getItemCount() == 0))
			return null;
		if (MainWindowShell.getProjects().size() != tf.getItemCount())  {
			System.out.println("The number of TabItems and OpenProjects are differents");
			return null;
		}
		return (Tree) tf.getItem(tf.getSelectionIndex()).getControl();
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

	public static void openProject(Project p) {
		if (projects.contains(p))
			return;
		if (!projects.add(p))
			return;
		TabFolder tabFolder = TabFolderInitializer.getTabFolder();

		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(p.getName());
		tabItem.setData(p);
		
		// create the tree
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
					TreeItem t = new TreeItem(i, SWT.NULL);
					t.setText("Slice");
					i.setExpanded(true);
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
					System.out.print(i.getText());
					TreeItem t = new TreeItem(i, SWT.NULL);
					t.setText("LIS Track");
					i.setExpanded(true);
				}
			}	    	
	    });
	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track Video");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					TreeItem t = new TreeItem(i, SWT.NULL);
					t.setText("Video Track");
					i.setExpanded(true);
				}
			}	    	
	    });
	    item = new MenuItem(contextMenuAnnotation, SWT.PUSH);
	    item.setText("Add Track Text");
	    item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (TreeItem i : tree.getSelection()) {
					TreeItem t = new TreeItem(i, SWT.NULL);
					t.setText("Text Track");
					i.setExpanded(true);
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
				TreeItem aItem = new TreeItem(tree, SWT.NULL);
				aItem.setText(new String [] {newAnnotationName,"Annotation","0 tracks"});
				aItem.setData(a);
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
	    
	    addProjectData(tree, p);

		tree.pack();
		tabItem.setControl(tree);
	}
	
    /** fill tree with project data
     * 
     * @param p
     */
	public static void addProjectData(Tree tree, Project p) {
	    for (Annotation a : p.getAnnotations()) {
	    	addAnnotationData(tree, a);
		}
	}

	private static void addAnnotationData(Tree tree, Annotation a) {
		TreeItem aItem = new TreeItem(tree, SWT.NONE);
		aItem.setText(new String[] { a.getName(), "annotation", String.valueOf(a.getTracks().size()) + " tracks"});
		aItem.setData(a);
		for (Track t : a.getTracks()) {
			addTrackData(aItem, t);
		}
	}

	private static void addTrackData(TreeItem aItem, Track t) {
		TreeItem tItem = new TreeItem(aItem, SWT.NONE);
		tItem.setText(new String[] { t.getName(), t.getTypeString() + " track", String.valueOf(t.getSlices().size()) + " slices"});
		tItem.setData(t);
		int i=0;
		for (Slice s : t.getSlices()) {
			addSliceData(tItem, s, i);
		}
	}

	private static void addSliceData(TreeItem tItem, Slice s, int i) {
		++i;
		TreeItem sItem = new TreeItem(tItem, SWT.NONE);
		sItem.setText(new String[] { "slice " + i, "slice", "duration: " + s.getStartTime() + " - " + s.getEndTime()});
		sItem.setData(s);
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
