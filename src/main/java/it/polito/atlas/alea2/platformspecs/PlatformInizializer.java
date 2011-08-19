package it.polito.atlas.alea2.platformspecs;

public abstract class PlatformInizializer {

	public void run() {
		if (isApplicable()) {
			doRun();
		}
	}

	protected abstract void doRun();

	protected abstract boolean isApplicable();

}
