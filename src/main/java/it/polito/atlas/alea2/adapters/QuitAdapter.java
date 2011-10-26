package it.polito.atlas.alea2.adapters;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class QuitAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		//ProjectManager.beforeClose();
		shell().close();
		//shell().getDisplay().dispose();
		System.exit(0);
	}
}
