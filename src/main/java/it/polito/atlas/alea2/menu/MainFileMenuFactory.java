package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.ImportMenuFactory.addImportMenu;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createSeparatorItem;
import it.polito.atlas.alea2.adapters.CloseProjectAdapter;
import it.polito.atlas.alea2.adapters.NewProjectAdapter;
import it.polito.atlas.alea2.adapters.OpenProjectAdapter;
import it.polito.atlas.alea2.adapters.OpenVideoAdapter;
import it.polito.atlas.alea2.adapters.QuitAdapter;
import it.polito.atlas.alea2.adapters.RemoveProjectAdapter;
import it.polito.atlas.alea2.adapters.RenameProjectAdapter;

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
		createItem(fileMenu, "&New Project", new NewProjectAdapter());
		createItem(fileMenu, "&Open Project...", new OpenProjectAdapter());
		createItem(fileMenu, "&Rename Project / Modify tags...", new RenameProjectAdapter());
		createItem(fileMenu, "&Close Project", new CloseProjectAdapter());
		createItem(fileMenu, "&Remove Project", new RemoveProjectAdapter());
		createSeparatorItem(fileMenu);
		addImportMenu(fileMenu);
		createSeparatorItem(fileMenu);
		createItem(fileMenu, "&Play a video file...", new OpenVideoAdapter());
		createSeparatorItem(fileMenu);
		createItem(fileMenu, "&Exit", new QuitAdapter());
		return fileMenu;
	}

}
