package it.polito.atlas.alea2.toolbar;

import static it.polito.atlas.alea2.components.ShellSingleton.shell;
import static org.eclipse.swt.SWT.PUSH;
import it.polito.atlas.alea2.adapters.OpenAdapter;
import it.polito.atlas.alea2.adapters.QuitAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolBarFactory {

	public static void createToolbar() {
		ToolBar toolbar = new ToolBar(shell(), SWT.BORDER);
		new ToolItem(toolbar, SWT.SEPARATOR);

		addTool(toolbar, "new.png", null);
		addTool(toolbar, "open.png", new OpenAdapter());
		addTool(toolbar, "quit.png", new QuitAdapter());
		toolbar.pack();

	}

	private static void addTool(ToolBar toolbar, String icon, SelectionAdapter adapter) {
		ToolItem toolItem = new ToolItem(toolbar, PUSH);
		try {
			toolItem.setImage(new Image(shell().getDisplay(), ToolBarFactory.class.getClassLoader().getResourceAsStream(icon)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (adapter != null) {
			toolItem.addSelectionListener(adapter);
		}
	}
}
