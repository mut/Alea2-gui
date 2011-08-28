package it.polito.atlas.alea2.initializer;

public abstract class Initializer {

	public void run() {
		if (isApplicable()) {
			doRun();
		}
	}

	protected abstract void doRun();

	protected abstract boolean isApplicable();

}
