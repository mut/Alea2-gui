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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
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
		instance.setText("Hello");
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
		projects.add(p);
		TabFolder tabFolder = TabFolderInitializer.getTabFolder();
		/*tabFolder.setToolTipText("Test toolTip");
		for (int i=1; i<5; i++) {
			// create a TabItem
			TabItem item = new TabItem( tabFolder, SWT.NULL);
			item.setText( "TabItem " + i);
			// create a control
			Label label = new Label( tabFolder, SWT.BORDER_SOLID);
			label.setText( "Page " + i);
			// add a control to the TabItem
			item.setControl( label );
		}*/
		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(p.getName());
		// create a control
		Tree tree = new Tree(tabFolder, SWT.SINGLE);
		for (Annotation a : p.getAnnotations()) {
			TreeItem aItem = new TreeItem(tree, 0);
			aItem.setText(a.getName());
			for (Track t : a.getTracks()) {
				TreeItem tItem = new TreeItem(aItem, 0);
				tItem.setText(t.getName());
				for (Slice s : t.getSlices()) {
					TreeItem sItem = new TreeItem(tItem, 0);
					sItem.setText(s.getStartTime() + " - " + s.getEndTime());
				}
			}
		}
		tree.pack();
		tabItem.setControl(tree);
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
