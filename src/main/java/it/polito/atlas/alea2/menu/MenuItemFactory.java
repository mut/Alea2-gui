package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemFactory {

	public static MenuItem createItem(Menu menu, String text, SelectionAdapter adapter) {
		MenuItem openItem = new MenuItem(menu, SWT.PUSH);
		openItem.setText(text);
		openItem.addSelectionListener(adapter);
		return openItem;
	}

	public static MenuItem createItem(Menu menu, String img, String icon, SelectionAdapter adapter) {
		MenuItem openItem = createItem(menu, img, adapter);
		openItem.setImage(new Image(shell().getDisplay(), shell().getClass().getClassLoader().getResourceAsStream(icon)));
		return openItem;
	}

	public static MenuItem createSeparatorItem(Menu menu) {
		MenuItem openItem = new MenuItem(menu, SWT.SEPARATOR);
		return openItem;
	}

}
