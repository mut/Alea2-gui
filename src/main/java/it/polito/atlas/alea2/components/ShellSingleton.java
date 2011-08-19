package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;
import it.polito.atlas.alea2.Project;

import org.eclipse.swt.widgets.Shell;

public class ShellSingleton {

	private static final Shell instance;

	private static boolean run;

	public static Shell shell() {
		return instance;
	}

	static {
		instance = new Shell(display());
		instance.setText("Hello");
	}

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
