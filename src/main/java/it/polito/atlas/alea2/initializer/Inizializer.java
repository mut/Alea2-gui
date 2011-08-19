package it.polito.atlas.alea2.initializer;

public abstract class Inizializer {

	public void run() {
		if (isApplicable()) {
			doRun();
		}
	}

	protected abstract void doRun();

	protected abstract boolean isApplicable();

}
