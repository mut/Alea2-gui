package it.polito.atlas.alea2.components;

import static it.polito.atlas.alea2.components.DisplaySingleton.display;
import it.polito.atlas.alea2.Annotation;
import it.polito.atlas.alea2.Project;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class CreateTextShell {
	private Shell shell;
	private Text textName;
	private String text = "";

	public Shell shell() {
		return shell;
	}

	public CreateTextShell(final Object sender, final Annotation a) {
		shell = new Shell(display());
		shell.setText("Insert new Track Text");
		shell.setSize(320, 200);
		shell.setLocation(SWT.DEFAULT, SWT.DEFAULT);
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent arg0) {
			}
			
			@Override
			public void shellDeiconified(ShellEvent arg0) {
			}
			
			@Override
			public void shellDeactivated(ShellEvent arg0) {
			}
			
			@Override
			public void shellClosed(ShellEvent arg0) {
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
			}
		});
		shell.setLayout(new FormLayout());
		
		Point size;
		FormData formData;
		
		// Label name
		Label labelName = new Label(shell(), SWT.BORDER);
		labelName.setText("Insert a Text, it will be splitted in slices for each space:");
		labelName.pack();
		size = labelName.getSize();
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(0);
		labelName.setLayoutData(formData);
		
		// Button OK
		Button ok = new Button(shell(), SWT.PUSH);
        ok.setText("OK");
		formData = new FormData(size.x, 40);
		formData.left = new FormAttachment(50);
		formData.right = new FormAttachment(100);
		//formData.top = new FormAttachment(textTags);
		formData.bottom = new FormAttachment(100);
		ok.setLayoutData(formData);
		ok.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				text = textName.getText();
				a.addTextTrack(text);
				if (sender instanceof Tree) {
					Tree tree = (Tree) sender;
					Project p = (Project) tree.getData();
					MainWindowShell.updateTree(tree, p);
				} else if (sender instanceof AnnotationShell) {
					AnnotationShell as = (AnnotationShell) sender;
					as.updateTree();
				}
				shell().close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		// Button Cancel
		Button cancel = new Button(shell(), SWT.PUSH);
		cancel.setText("Cancel");
		formData = new FormData(size.x, 40);
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(50);
		//formData.top = new FormAttachment(textTags);
		formData.bottom = new FormAttachment(100);
		cancel.setLayoutData(formData);
		cancel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell().close();				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});		
		
		// Text name
		textName = new Text(shell(), SWT.SINGLE | SWT.BORDER);
		textName.setText("");
		textName.pack();
		size = textName.getSize();
		formData = new FormData(size.x, size.y);
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(labelName);
		formData.bottom = new FormAttachment(ok);
		textName.setLayoutData(formData);
		
		shell.open();
	}
	
	public CreateTextShell(Listener listener, Tree currentTree) {
		// TODO Auto-generated constructor stub
	}

	public String getText() {
		return text;
	}

}
