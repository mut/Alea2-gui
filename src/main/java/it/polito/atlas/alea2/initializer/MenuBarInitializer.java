package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MainFileMenuFactory.addFileMenu;
import static it.polito.atlas.alea2.menu.ViewMenuFactory.addViewMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

public class MenuBarInitializer extends Initializer {

	private static final MenuBarInitializer instance = new MenuBarInitializer();

	@Override
	protected void doRun() {
		shell().setMenuBar(createMenu());
	}

	private static Menu createMenu() {
		Menu menuBar = new Menu(shell(), SWT.BAR);
		addFileMenu(menuBar);
		addViewMenu(menuBar);
		return menuBar;
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer menuBarInizializer() {
		return instance;
	}

}