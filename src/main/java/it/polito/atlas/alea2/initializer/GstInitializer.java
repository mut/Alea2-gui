package it.polito.atlas.alea2.initializer;

import org.gstreamer.Gst;

/**
 * @author  DANGELOA
 */
public class GstInitializer extends Initializer {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static final GstInitializer instance = new GstInitializer();

	@Override
	protected void doRun() {
		Gst.init("Alea2", new String[] { "", "" });
	}

	@Override
	protected boolean isApplicable() {
		return true;
	}

	public static Initializer gstInitializer() {
		return instance;
	}

}
