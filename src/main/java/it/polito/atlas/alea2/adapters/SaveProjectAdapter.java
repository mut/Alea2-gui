package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SaveProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Storage st=MainWindowShell.getStorage();
		Project p=MainWindowShell.getCurrentProject();
		if  ((p!=null) && (st!=null))
			st.writeProject(p);
	}

}
