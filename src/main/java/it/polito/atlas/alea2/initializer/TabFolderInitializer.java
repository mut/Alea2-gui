package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowShell.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


public class TabFolderInitializer extends Initializer {

	private static final TabFolderInitializer instance = new TabFolderInitializer();
	
	private static TabFolder tabFolder;

	@Override
	protected void doRun() {
		tabFolder = new TabFolder(shell(), SWT.BORDER_SOLID);
		/*tabFolder.setToolTipText("Test toolTip");
		for (int i=1; i<5; i++) {
			// create a TabItem
			TabItem item = new TabItem( tabFolder, SWT.NULL);
			item.setText( "TabItem " + i);
			// create a control
			Label label = new Label( tabFolder, SWT.BORDER_SOLID);
			label.setText( "Page " + i);
			// add a control to the TabItem
			item.setControl( label );
		}*/
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer tabFolderInizializer() {
		return instance;
	}
	
	public static TabFolder getTabFolder() {
		return tabFolder;
	}

}

