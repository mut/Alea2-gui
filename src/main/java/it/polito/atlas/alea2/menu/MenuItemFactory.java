package it.polito.atlas.alea2.menu;

import static org.eclipse.swt.SWT.PUSH;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemFactory {

	public static MenuItem createItem(Menu menu, String text, SelectionAdapter adapter) {
		MenuItem openItem = new MenuItem(menu, PUSH);
		openItem.setText(text);
		openItem.addSelectionListener(adapter);
		return openItem;
	}

}
