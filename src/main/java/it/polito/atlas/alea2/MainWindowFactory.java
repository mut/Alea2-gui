package it.polito.atlas.alea2;

import static it.polito.atlas.alea2.components.ShellSingleton.runShell;
import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static it.polito.atlas.alea2.components.StatusBar.initStatus;
import static it.polito.atlas.alea2.menu.MenuBarFactory.createMenu;
import static it.polito.atlas.alea2.platformspecs.MacPlatformInizializer.macPlatformInizializer;
import static it.polito.atlas.alea2.toolbar.ToolBarFactory.createToolbar;
import it.polito.atlas.alea2.platformspecs.PlatformInizializer;

import java.util.ArrayList;
import java.util.List;

public class MainWindowFactory {

	private static MainWindowFactory instance = new MainWindowFactory();

	private static final List<PlatformInizializer> platformInizializers = new ArrayList<PlatformInizializer>();
	static {
		platformInizializers.add(macPlatformInizializer());
	}

	public static void showWindow() {
		for (PlatformInizializer initializer : platformInizializers) {
			initializer.run();
		}
		instance.build();
	}

	private void build() {
		initStatus();
		shell().setMenuBar(createMenu());
		createToolbar();
//		initUIPopUp(shell());
		runShell();
	}

	//TODO serve?
//	void initUIPopUp(Decorations d) {
//		Menu menu = new Menu(d, SWT.POP_UP);
//
//		MenuItem minItem = new MenuItem(menu, SWT.PUSH);
//		minItem.setText("Minimize");
//
//		minItem.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				shell().setMinimized(true);
//			}
//		});
//
//		MenuItem exitItem = new MenuItem(menu, SWT.PUSH);
//		exitItem.setText("Exit");
//
//		exitItem.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				shell().getDisplay().dispose();
//				System.exit(0);
//			}
//		});
//
//		shell().setMenu(menu);
//	}

}
