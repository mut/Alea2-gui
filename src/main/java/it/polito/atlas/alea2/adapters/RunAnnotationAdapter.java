package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.AnnotationShell;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;

public class RunAnnotationAdapter extends SelectionAdapter {
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p = MainWindowShell.getCurrentProject();
		if (p == null)
			return;
		
		Tree tree = MainWindowShell.getCurrentTree();
		if (tree == null)
			return;
		
		AnnotationShell as = new AnnotationShell(tree);
		MainWindowShell.annotationShells.add(as);
		as.getPlayer().open();
		
	}
}
