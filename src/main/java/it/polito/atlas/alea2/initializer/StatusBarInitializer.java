package it.polito.atlas.alea2.initializer;

import static it.polito.atlas.alea2.components.MainWindowStatusBar.initStatus;

public class StatusBarInitializer extends Initializer {

	private static final StatusBarInitializer instance = new StatusBarInitializer();

	@Override
	protected void doRun() {
		initStatus();
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer statusBarInizializer() {
		return instance;
	}
}
