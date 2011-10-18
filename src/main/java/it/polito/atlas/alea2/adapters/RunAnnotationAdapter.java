package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.AnnotationShell;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RunAnnotationAdapter extends SelectionAdapter {
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p = MainWindowShell.getCurrentProject();
		if (p == null)
			return;
		
		Annotation a = p.getCurrentAnnotation();
		if (a == null)
			return;
		
		AnnotationShell as = new AnnotationShell(a);
		MainWindowShell.annotationShells.add(as);
		
	}
}
