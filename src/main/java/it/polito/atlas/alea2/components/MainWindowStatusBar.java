package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import it.polito.atlas.alea2.errors.UninitializedComponentException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;

public class MainWindowStatusBar {

	private static Label instance;

	public static Label status() {
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
		Label status = new Label(shell(), SWT.BORDER);
		status.setText("Ready");
		FormLayout layout = new FormLayout();
		shell().setLayout(layout);

		FormData labelData = new FormData();
		labelData.left = new FormAttachment(0);
		labelData.right = new FormAttachment(100);
		labelData.bottom = new FormAttachment(100);
		status.setLayoutData(labelData);
		return status;
	}

}
