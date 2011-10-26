package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import it.polito.atlas.alea2.adapters.AboutAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MainHelpMenuFactory {

	public static void addHelpMenu(Menu menu) {
		MenuItem cascadeHelpMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeHelpMenu.setText("&Help");
		cascadeHelpMenu.setMenu(createHelpMenu());
	}

	private static Menu createHelpMenu() {
		Menu helpMenu = new Menu(shell(), SWT.DROP_DOWN);
		createItem(helpMenu, "&About", new AboutAdapter());
		return helpMenu;
	}

}
