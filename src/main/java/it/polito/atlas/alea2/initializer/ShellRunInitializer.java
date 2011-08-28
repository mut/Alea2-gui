package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowShell.runShell;

public class ShellRunInitializer extends Initializer {

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
