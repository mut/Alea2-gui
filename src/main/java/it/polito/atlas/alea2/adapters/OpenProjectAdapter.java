package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.db.DBStorage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class OpenProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		List<Project> projects = new ArrayList<Project>();
		Storage st = new DBStorage();
		for (String s : st.getProjectNamesList()) {
			System.out.println(s);
			projects.add(st.readProject(s));
		}
	}

}
