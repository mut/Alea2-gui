package it.polito.atlas.alea2.menu.viewmenu;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static it.polito.atlas.alea2.menu.viewmenu.StatListener.addStatItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ViewMenuFactory {

	public static void addViewMenu(Menu menu) {
		MenuItem cascadeViewMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeViewMenu.setText("&View");
		
		Menu viewMenu = new Menu(shell(), SWT.DROP_DOWN);
		addStatItem(viewMenu);
		
		cascadeViewMenu.setMenu(viewMenu);
	}

}
