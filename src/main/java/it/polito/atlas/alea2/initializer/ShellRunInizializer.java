package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.ShellSingleton.runShell;

public class ShellRunInizializer extends Inizializer {

	private static ShellRunInizializer instance = new ShellRunInizializer();

	@Override
	protected void doRun() {
		runShell();
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Inizializer shellRunInizializer() {
		return instance;
	}

}
