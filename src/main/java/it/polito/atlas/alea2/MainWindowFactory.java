package it.polito.atlas.alea2;

import static it.polito.atlas.alea2.initializer.GstInitializer.gstInitializer;
import static it.polito.atlas.alea2.initializer.MacPlatformInizializer.macPlatformInizializer;
import static it.polito.atlas.alea2.initializer.MenuBarInizializer.menuBarInizializer;
import static it.polito.atlas.alea2.initializer.ShellRunInizializer.shellRunInizializer;
import static it.polito.atlas.alea2.initializer.StatusBarInizializer.statusBarInizializer;
import static it.polito.atlas.alea2.initializer.ToolBarInizializer.toolBarInizializer;
import it.polito.atlas.alea2.initializer.Inizializer;

import java.util.ArrayList;
import java.util.List;

public class MainWindowFactory {

	private static final List<Inizializer> inizializers = new ArrayList<Inizializer>();
	static {
		inizializers.add(gstInitializer());
		inizializers.add(macPlatformInizializer());
		inizializers.add(statusBarInizializer());
		inizializers.add(menuBarInizializer());
		inizializers.add(toolBarInizializer());
		inizializers.add(shellRunInizializer());
	}

	public static void showWindow() {
		for (Inizializer initializer : inizializers) {
			initializer.run();
		}
	}

}
