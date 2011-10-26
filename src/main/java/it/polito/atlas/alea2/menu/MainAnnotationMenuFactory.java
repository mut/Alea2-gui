package it.polito.atlas.alea2.menu;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createItem;
import static it.polito.atlas.alea2.menu.MenuItemFactory.createSeparatorItem;
import it.polito.atlas.alea2.adapters.NewAnnotationAdapter;
import it.polito.atlas.alea2.adapters.RunAnnotationAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MainAnnotationMenuFactory {

	public static void addAnnotationMenu(Menu menu) {
		MenuItem cascadeAnnotationMenu = new MenuItem(menu, SWT.CASCADE);
		cascadeAnnotationMenu.setText("&Annotation");
		cascadeAnnotationMenu.setMenu(createAnnotationMenu());
	}

	private static Menu createAnnotationMenu() {
		Menu annotationMenu = new Menu(shell(), SWT.DROP_DOWN);
		createItem(annotationMenu, "&Add Annotation", new NewAnnotationAdapter());
		createSeparatorItem(annotationMenu);
		createItem(annotationMenu, "&Play Annotation", new RunAnnotationAdapter());
		return annotationMenu;
	}

}
