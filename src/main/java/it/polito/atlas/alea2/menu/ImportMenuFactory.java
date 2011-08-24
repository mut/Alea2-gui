package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import it.polito.atlas.alea2.adapters.BookmarkAdapter;
import it.polito.atlas.alea2.adapters.MailAdapter;
import it.polito.atlas.alea2.adapters.OpenAdapter;
import it.polito.atlas.alea2.adapters.OpenProjectAdapter;

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
		createItem(submenu, "&open project...", new OpenProjectAdapter());
		createItem(submenu, "&open file...", new OpenAdapter());
		createItem(submenu, "&Import bookmarks...", new BookmarkAdapter());
		createItem(submenu, "&Import mail...", new MailAdapter());
		return submenu;
	}

}
