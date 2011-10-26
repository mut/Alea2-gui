package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createSeparatorItem;
import it.polito.atlas.alea2.adapters.OpenVideoAdapter;
import it.polito.atlas.alea2.adapters.QuitAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MainFileMenuFactory {

	public static void addFileMenu(Menu menu) {
		MenuItem cascadeFileMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeFileMenu.setText("&File");
		cascadeFileMenu.setMenu(createFileMenu());
	}

	private static Menu createFileMenu() {
		Menu fileMenu = new Menu(shell(), SWT.DROP_DOWN);
		createItem(fileMenu, "&Play a video file...", new OpenVideoAdapter());
		createSeparatorItem(fileMenu);
		createItem(fileMenu, "&Exit", new QuitAdapter());
		return fileMenu;
	}

}
