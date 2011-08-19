package it.polito.atlas.alea2.menu.filemenu;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static org.eclipse.swt.SWT.PUSH;
import it.polito.atlas.alea2.errors.UninitializedComponentException;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ExitAdapter extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		shell().getDisplay().dispose();
		System.exit(0);
	}

	private static MenuItem instance;

	public static MenuItem exitItem() {
		if (instance == null) {
			throw new UninitializedComponentException();
		}
		return instance;
	}

	public static MenuItem addExitItem(Menu menu) {
		if (instance == null) {
			instance = init(menu);
		}
		return instance;
	}

	private static MenuItem init(Menu menu) {
		MenuItem exitItem = new MenuItem(menu, PUSH);
		exitItem.setText("&Exit");
		exitItem.addSelectionListener(new ExitAdapter());
		return exitItem;
	}

}
