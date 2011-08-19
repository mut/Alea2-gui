package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static it.polito.atlas.alea2.menu.filemenu.FileMenuFactory.addFileMenu;
import static it.polito.atlas.alea2.menu.viewmenu.ViewMenuFactory.addViewMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

public class MenuBarFactory {
	public static Menu createMenu() {
		Menu menuBar = new Menu(shell(), SWT.BAR);
		addFileMenu(menuBar);
		addViewMenu(menuBar);
		return menuBar;
	}
}
