package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewAnnotationAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p = MainWindowShell.getCurrentProject();
		if (p == null)
			return;
    	String newAnnotationName = "Annotation " + (p.getAnnotations().size() + 1);
    	Annotation a = new Annotation(p, newAnnotationName);
		p.addAnnotation(a);
    	MainWindowShell.updateTree(MainWindowShell.getCurrentTree(), p);
	}	    	
}
