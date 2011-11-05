package it.polito.atlas.alea2.initializer;

/**
 * @author  DANGELOA
 */
public class MacPlatformInitializer extends Initializer {

	/**
	 * Create the instance of this initializer
	 */
	private static MacPlatformInitializer instance = new MacPlatformInitializer();

	/**
	 * Returns the instance of this initializer
	 */
	public static Initializer macPlatformInizializer() {
		return instance;
	}

	/**
	 * Executes the initializer
	 */
	@Override
	protected void doRun() {
		final String jnaLibraryPath = System.getProperty("jna.library.path");
		final StringBuilder newJnaLibraryPath = new StringBuilder(jnaLibraryPath != null ? (jnaLibraryPath + ":") : "");
		newJnaLibraryPath.append("/System/Library/Frameworks/GStreamer.framework/Versions/0.10-" + (com.sun.jna.Platform.is64Bit() ? "x64" : "i386") + "/lib:");
		System.setProperty("jna.library.path", newJnaLibraryPath.toString());
		System.out.println("jna.library.path = " + newJnaLibraryPath);
	}

	/**
	 * Check if this initializer is needed
	 */
	@Override
	protected boolean isApplicable() {
		return com.sun.jna.Platform.isMac();
	}
}
