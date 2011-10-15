package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import it.polito.atlas.alea2.adapters.OpenVideoAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ImportMenuFactory {

	public static void addImportMenu(Menu menu) {
		MenuItem menuImport = new MenuItem(menu, SWT.CASCADE);
		menuImport.setText("Import");
		menuImport.setMenu(subMenu());
	}

	private static Menu subMenu() {
		Menu submenu = new Menu(shell(), SWT.DROP_DOWN);
		createItem(submenu, "&open video file...", new OpenVideoAdapter());
		return submenu;
	}

}
