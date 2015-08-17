package ru.splat.DesktopClient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Main class for launching a Desktop Client
 */
public class ClientLauncher {
	static private final Logger log = LoggerFactory.getLogger(ClientLauncher.class);
	
	public static void main(String[] args) {
        Display display = new Display(); 
        log.info("Display was created");
        
        Shell shell = new Shell(display);
		shell.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.KeyDown && event.character == SWT.ESC) {
					shell.dispose();
				}
			}
		});
		
        shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		log.info("Display was disposed");
	}
}
