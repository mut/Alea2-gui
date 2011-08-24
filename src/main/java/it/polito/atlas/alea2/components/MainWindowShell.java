package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import java.util.ArrayList;
import java.util.List;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.db.DBStorage;

import org.eclipse.swt.widgets.Shell;

public class MainWindowShell {

	private static final Shell instance;

	private static boolean run;

	public static Shell shell() {
		return instance;
	}

	static {
		instance = new Shell(display());
		instance.setText("Hello");
	}

	/**
	 * Elenco dei progetti aperti
	 */
	private static List<Project> projects = new ArrayList<Project>();

	/**
	 * @return the projects
	 */
	public static List<Project> getProjects() {
		return projects;
	}

	/**
	 * @return the storage
	 */
	public static Storage getStorage() {
		return storage;
	}

	/**
	 * Storage dei progetti
	 */
	private static Storage storage = new DBStorage();

	public static void runShell() {
		if (!run) {
			instance.setSize(250, 200);
			instance.setLocation(300, 300);

			instance.open();
			instance.setToolTipText(new Project("test").getName());

			while (!instance.isDisposed()) {
				if (!display().readAndDispatch()) {
					display().sleep();
				}
			}
			display().dispose();
		}
	}
}
