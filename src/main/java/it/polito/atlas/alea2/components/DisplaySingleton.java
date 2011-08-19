package it.polito.atlas.alea2.components;

import org.eclipse.swt.widgets.Display;

public class DisplaySingleton {

	private static final Display instance = new Display();

	public static Display display() {
		return instance;
	}
}
