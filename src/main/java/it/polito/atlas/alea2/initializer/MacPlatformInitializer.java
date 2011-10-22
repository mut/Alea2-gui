package it.polito.atlas.alea2.initializer;

/**
 * @author  DANGELOA
 */
public class MacPlatformInitializer extends Initializer {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static MacPlatformInitializer instance = new MacPlatformInitializer();

	public static Initializer macPlatformInizializer() {
		return instance;
	}

	@Override
	protected void doRun() {
		final String jnaLibraryPath = System.getProperty("jna.library.path");
		final StringBuilder newJnaLibraryPath = new StringBuilder(jnaLibraryPath != null ? (jnaLibraryPath + ":") : "");
		newJnaLibraryPath.append("/System/Library/Frameworks/GStreamer.framework/Versions/0.10-" + (com.sun.jna.Platform.is64Bit() ? "x64" : "i386") + "/lib:");
		System.setProperty("jna.library.path", newJnaLibraryPath.toString());
		System.out.println("jna.library.path = " + newJnaLibraryPath);
	}

	@Override
	protected boolean isApplicable() {
		return com.sun.jna.Platform.isMac();
	}
}
