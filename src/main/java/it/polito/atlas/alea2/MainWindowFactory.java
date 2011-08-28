package it.polito.atlas.alea2;

import static it.polito.atlas.alea2.initializer.GstInitializer.gstInitializer;
import static it.polito.atlas.alea2.initializer.MacPlatformInitializer.macPlatformInizializer;
import static it.polito.atlas.alea2.initializer.MenuBarInitializer.menuBarInizializer;
import static it.polito.atlas.alea2.initializer.ShellRunInitializer.shellRunInizializer;
import static it.polito.atlas.alea2.initializer.StatusBarInitializer.statusBarInizializer;
import static it.polito.atlas.alea2.initializer.TabFolderInitializer.tabFolderInizializer;
import static it.polito.atlas.alea2.initializer.ToolBarInitializer.toolBarInizializer;
import it.polito.atlas.alea2.initializer.Initializer;

import java.util.ArrayList;
import java.util.List;

public class MainWindowFactory {

	private static final List<Initializer> inizializers = new ArrayList<Initializer>();
	
	static {
		inizializers.add(gstInitializer());
		inizializers.add(macPlatformInizializer());
		inizializers.add(statusBarInizializer());
		inizializers.add(menuBarInizializer());
		inizializers.add(toolBarInizializer());
		inizializers.add(tabFolderInizializer());
		inizializers.add(shellRunInizializer());
	}

	public static void showWindow() {
		for (Initializer initializer : inizializers) {
			initializer.run();
		}
	}

}
