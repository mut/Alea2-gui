package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.components.NewProjectShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		new NewProjectShell();
	}

}
