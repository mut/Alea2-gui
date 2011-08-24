package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class OpenProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Storage st=MainWindowShell.getStorage();
		for (String s : st.getProjectNamesList()) {
			System.out.println(s);
			MainWindowShell.getProjects().add(st.readProject(s));
		}
	}

}
