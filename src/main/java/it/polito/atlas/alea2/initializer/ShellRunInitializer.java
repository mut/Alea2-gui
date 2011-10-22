package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowShell.runShell;

/**
 * @author  DANGELOA
 */
public class ShellRunInitializer extends Initializer {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static ShellRunInitializer instance = new ShellRunInitializer();

	@Override
	protected void doRun() {
		runShell();
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer shellRunInizializer() {
		return instance;
	}

}
