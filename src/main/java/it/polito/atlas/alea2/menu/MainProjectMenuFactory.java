package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createSeparatorItem;
import it.polito.atlas.alea2.adapters.CloseProjectAdapter;
import it.polito.atlas.alea2.adapters.NewProjectAdapter;
import it.polito.atlas.alea2.adapters.OpenProjectAdapter;
import it.polito.atlas.alea2.adapters.RemoveProjectAdapter;
import it.polito.atlas.alea2.adapters.RenameProjectAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MainProjectMenuFactory {

	public static void addProjectMenu(Menu menu) {
		MenuItem cascadeProjectMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeProjectMenu.setText("&Project");
		cascadeProjectMenu.setMenu(createProjectMenu());
	}

	private static Menu createProjectMenu() {
		Menu projectMenu = new Menu(shell(), SWT.DROP_DOWN);
		createItem(projectMenu, "&New Project", "new.png", new NewProjectAdapter());
		createItem(projectMenu, "&Open Project...", "open.png", new OpenProjectAdapter());
		createItem(projectMenu, "&Rename Project / Modify tags...", new RenameProjectAdapter());
		createItem(projectMenu, "&Close Project", "close.png", new CloseProjectAdapter());
		createSeparatorItem(projectMenu);
		createItem(projectMenu, "&Remove Project", "remove.png", new RemoveProjectAdapter());
		return projectMenu;
	}

}
