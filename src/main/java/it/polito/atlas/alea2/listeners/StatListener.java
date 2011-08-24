package it.polito.atlas.alea2.listeners;

import static it.polito.atlas.alea2.components.MainWindowStatusBar.status;
import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.Selection;
import it.polito.atlas.alea2.errors.UninitializedComponentException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class StatListener implements Listener {


	@Override
	public void handleEvent(Event event) {
		if (instance.getSelection()) {
			status().setVisible(true);
		} else {
			status().setVisible(false);
		}
	}

	private static MenuItem instance;

	public static MenuItem statItem() {
		if (instance == null) {
			throw new UninitializedComponentException();
		}
		return instance;
	}

	public static MenuItem addStatItem(Menu menu) {
		if (instance == null) {
			instance = init(menu);
		}
		return instance;
	}

	private static MenuItem init(Menu menu) {
		MenuItem statItem = new MenuItem(menu, CHECK);
		statItem.setSelection(true);
		statItem.setText("&View Statusbar");
		statItem.addListener(Selection, new StatListener());
		return statItem;
	}

}
