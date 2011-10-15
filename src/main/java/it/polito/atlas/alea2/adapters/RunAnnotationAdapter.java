package it.polito.atlas.alea2.adapters;

import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;
import it.polito.atlas.alea2.TrackVideo;
import it.polito.atlas.alea2.components.MainWindowShell;
import it.polito.atlas.alea2.components.SWTPlayer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RunAnnotationAdapter extends SelectionAdapter {
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Project p = MainWindowShell.getCurrentProject();
		if (p == null)
			return;
		
		Annotation a = p.getCurrentAnnotation();
		if (a == null)
			return;
		
		SWTPlayer pl = new SWTPlayer();
		
		for (TrackVideo t :a.getTracksVideo()) {
			System.out.println(t.getName());			
			pl.addVideo(t.getName());
		}
		
		pl.play();
		
	}
}
