package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.components.MainWindowShell;
import it.polito.atlas.alea2.functions.ProjectManager;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SaveProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		ProjectManager.saveProject(MainWindowShell.getStorage(), MainWindowShell.getCurrentProject());
	}

}
