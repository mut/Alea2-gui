package it.polito.atlas.alea2.functions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.Storage;
import it.polito.atlas.alea2.components.MainWindowShell;

public class ProjectManager {
	public static boolean saveProject(Storage st, Project p) {
		if ((st == null) || (p == null))
			return false;
		if (st.containsProject(p.getName())) {
			MessageBox mb = new MessageBox(MainWindowShell.shell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			mb.setText("Save on Storage");
			mb.setMessage("The prject already exist in current Storage, would you overwrite?");
			int rc = mb.open();
		    switch (rc) {
		    	case SWT.YES:
		    		break;
		    	case SWT.NO:
		    		return false;
		    }
		}
		if (st.writeProject(p, true)) {
			p.setModified(false);
			return true;
		}
		return false;
	}
}
