package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.MainWindowShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;

public class RemoveProjectAdapter extends SelectionAdapter {
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p=MainWindowShell.getCurrentProject();
		if (p==null)
			return;
		Tree tree=(Tree)p.link;
		if (tree==null)
			return;

		// Chiede conferma
		MessageBox mb = new MessageBox(MainWindowShell.shell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setMessage("Are you sure to delete the current Project?");
		mb.setText("Delete Project from Storage");
		switch (mb.open()) {
			case SWT.NO:
				return;
		}
		
		// Elimina dalla finestra
		TabFolder tf=(TabFolder) tree.getParent();
		tree.dispose();
		int i = tf.getSelectionIndex();
		TabItem ti = tf.getItem(i);
		if (ti == null)
			return;
		ti.dispose();
		
		// Elimina dall'elenco dei progetti aperti
		MainWindowShell.getProjects().remove(i);

		// Elimina dallo Storage
		MainWindowShell.getStorage().deleteProject(p.getName());		
	}
}
