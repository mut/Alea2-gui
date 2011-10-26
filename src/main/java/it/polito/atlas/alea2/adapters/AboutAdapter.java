package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.components.AboutShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AboutAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		new AboutShell();
	}	    	
}
