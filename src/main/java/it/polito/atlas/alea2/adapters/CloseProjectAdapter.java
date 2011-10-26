package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.components.MainWindowShell;
import it.polito.atlas.alea2.functions.ProjectManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;

public class CloseProjectAdapter extends SelectionAdapter {
	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p=MainWindowShell.getCurrentProject();
		if (p==null)
			return;
		Tree tree=(Tree)p.link;
		if (tree==null)
			return;

		if (p.isModified()) {
			MessageBox mb = new MessageBox(MainWindowShell.shell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO | SWT.CANCEL);
			mb.setMessage("The current Project has been modified\nSave the current Project before closing?");
			mb.setText("Close Project");
			switch (mb.open()) {
				case SWT.CANCEL:
					return;
				case SWT.YES:
					ProjectManager.saveProject(MainWindowShell.getStorage(), p);
					break;
			}
		}

		TabFolder tf=(TabFolder) tree.getParent();
		tree.dispose();
		int i = tf.getSelectionIndex();
		TabItem ti = tf.getItem(i);
		if (ti == null)
			return;
		ti.dispose();
		MainWindowShell.getProjects().remove(i);
		
	}
}
