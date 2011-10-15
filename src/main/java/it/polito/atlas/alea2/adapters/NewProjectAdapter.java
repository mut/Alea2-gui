package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewProjectAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p=new Project("New Project");
		p.addAnnotation(new Annotation("New Annotation"));
		MainWindowShell.openProject(p);
	}

}
