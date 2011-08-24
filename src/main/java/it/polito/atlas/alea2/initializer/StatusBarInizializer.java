package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowStatusBar.initStatus;

public class StatusBarInizializer extends Inizializer {

	private static final StatusBarInizializer instance = new StatusBarInizializer();

	@Override
	protected void doRun() {
		initStatus();
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Inizializer statusBarInizializer() {
		return instance;
	}

}
