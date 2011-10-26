package it.polito.atlas.alea2.components;

import org.eclipse.swt.widgets.MessageBox;

public class AboutShell {
	public AboutShell() {
		MessageBox mb = new MessageBox(MainWindowShell.shell());
		mb.setText("About ALEA2");
		mb.setMessage("ALEA2");
		mb.open();
	}
}
