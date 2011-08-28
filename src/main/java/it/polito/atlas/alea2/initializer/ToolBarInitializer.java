package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;
import static org.eclipse.swt.SWT.PUSH;
import it.polito.atlas.alea2.adapters.OpenAdapter;
import it.polito.atlas.alea2.adapters.QuitAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolBarInitializer extends Initializer {

	private static final ToolBarInitializer instance = new ToolBarInitializer();
	private static CoolBar coolBar;
	/**
	 * @return the coolBar
	 */
	public static CoolBar getCoolBar() {
		return coolBar;
	}

	@Override
	protected void doRun() {
		coolBar = new CoolBar(shell(), SWT.BORDER_SOLID);
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
		
		addTool(toolBar, "new.png", null);
		addTool(toolBar, "open.png", new OpenAdapter());
		addSeparator(toolBar);
		addTool(toolBar, "quit.png", new QuitAdapter());
		toolBar.pack();

		// Add a coolItem to a coolBar
		CoolItem coolItem = new CoolItem(coolBar, SWT.NULL);
		// set the control of the coolItem
		coolItem.setControl(toolBar);
		// You have to specify the size
		
		// You have to specify the size  
		Point size = toolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT); 
		Point coolSize = coolItem.computeSize(size.x, size.y);
		coolItem.setSize(coolSize);
		coolBar.pack();
		size = coolBar.getSize();
		FormData coolData = new FormData(size.x, size.y);
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top= new FormAttachment(0);
		//coolData.bottom = new FormAttachment(5);*/
		coolBar.setLayoutData(coolData);
	}

	private void addSeparator(ToolBar toolBar) {
		new ToolItem(toolBar, SWT.SEPARATOR);
	}

	private void addTool(ToolBar toolBar, String icon, SelectionAdapter adapter) {
		ToolItem toolItem = new ToolItem(toolBar, PUSH);
		try {
			toolItem.setImage(new Image(shell().getDisplay(), getClass().getClassLoader().getResourceAsStream(icon)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (adapter != null) {
			toolItem.addSelectionListener(adapter);
		}
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer toolBarInizializer() {
		return instance;
	}

}
