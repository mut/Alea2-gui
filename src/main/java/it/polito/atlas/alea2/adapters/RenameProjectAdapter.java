package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.components.MainWindowShell;
import it.polito.atlas.alea2.components.RenameProjectShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RenameProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		new RenameProjectShell(MainWindowShell.getCurrentProject());
	}

}
