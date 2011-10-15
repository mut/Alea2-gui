package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Shell;

public class NewProjectShell extends Shell {
	private static final Shell instance;
	private static final FormLayout layout;


	public static Shell shell() {
		return instance;
	}

	static {
		instance = new Shell(display());
		instance.setText("New Project");
		layout = new FormLayout();
		instance.setLayout(layout);
	}
	
	public NewProjectShell() {
		instance.setSize(320, 240);
		instance.setLocation(SWT.DEFAULT, SWT.DEFAULT);
		instance.open();
	}
}
