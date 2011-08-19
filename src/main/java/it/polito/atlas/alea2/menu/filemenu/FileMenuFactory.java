package it.polito.atlas.alea2.menu.filemenu;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static it.polito.atlas.alea2.menu.filemenu.ExitAdapter.addExitItem;
import static it.polito.atlas.alea2.menu.filemenu.importmenu.ImportMenuFactory.addImportMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class FileMenuFactory {

	public static void addFileMenu(Menu menu) {
		MenuItem cascadeFileMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeFileMenu.setText("&File");
		cascadeFileMenu.setMenu(createFileMenu());
	}

	private static Menu createFileMenu() {
		Menu fileMenu = new Menu(shell(), SWT.DROP_DOWN);
		addImportMenu(fileMenu);
		addExitItem(fileMenu);
		return fileMenu;
	}

}
