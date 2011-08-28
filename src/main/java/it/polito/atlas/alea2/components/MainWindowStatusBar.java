package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import it.polito.atlas.alea2.errors.UninitializedComponentException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Label;

public class MainWindowStatusBar {

	private static Label instance;

	public static Label getStatusBar() {
		if (instance == null) {
			throw new UninitializedComponentException();
		}
		return instance;
	}

	public static Label initStatus() {
		if (instance == null) {
			instance = init();
		}
		return instance;
	}

	/**
	 * @return
	 */
	private static Label init() {
		instance = new Label(shell(), SWT.BORDER);
		instance.setText("Ready");
		instance.pack();
		Point size = instance.getSize();
		FormData labelData = new FormData(size.x, size.y);
		labelData.left = new FormAttachment(0);
		labelData.right = new FormAttachment(100);
		labelData.bottom = new FormAttachment(100);
		instance.setLayoutData(labelData);
		return instance;
	}

}
