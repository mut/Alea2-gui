package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class QuitButtonAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		shell().getDisplay().dispose();
		System.exit(0);
	}

}
