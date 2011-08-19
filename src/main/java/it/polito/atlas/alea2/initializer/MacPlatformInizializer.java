package it.polito.atlas.alea2.initializer;

public class MacPlatformInizializer extends Inizializer {

	private static MacPlatformInizializer instance = new MacPlatformInizializer();

	public static Inizializer macPlatformInizializer() {
		return instance;
	}

	@Override
	protected void doRun() {
		final String jnaLibraryPath = System.getProperty("jna.library.path");
		final StringBuilder newJnaLibraryPath = new StringBuilder(jnaLibraryPath != null ? (jnaLibraryPath + ":") : "");
		newJnaLibraryPath.append("/System/Library/Frameworks/GStreamer.framework/Versions/0.10-" + (com.sun.jna.Platform.is64Bit() ? "x64" : "i386") + "/lib:");
		System.setProperty("jna.library.path", newJnaLibraryPath.toString());
	}

	@Override
	protected boolean isApplicable() {
		return com.sun.jna.Platform.isMac();
	}
}
