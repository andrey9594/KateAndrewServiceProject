package ru.splat.DesktopClient.listeners;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *
 * @author Ekaterina
 *         Draw graph Value(id) by SWT lib
 */
public class GraphListener implements SelectionListener {
    private static final Logger log = LoggerFactory.getLogger(GraphListener.class);

    private int providerId;
    private Shell shell;
    private GC gc;

    public GraphListener(Shell shlDesktopClient, int provider) {
        this.shell = shlDesktopClient;
        this.providerId = provider;
    }

    @Override
    public void widgetSelected(SelectionEvent selectionEvent) {
        shell.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                paintEvent.gc.drawLine(20, 25, 20, 425);//Y(Value)
                paintEvent.gc.drawLine(20, 25, 25, 40);
                paintEvent.gc.drawLine(20, 25, 15, 40);
                paintEvent.gc.drawString("Value", 30, 25);

                paintEvent.gc.drawLine(20, 225, 490, 225);//X(id)
                paintEvent.gc.drawLine(473, 220, 490, 225);
                paintEvent.gc.drawLine(473, 230, 490, 225);
                paintEvent.gc.drawString("id", 460, 205);

                //Draw a curve
                if (providerId == 0) {
                    int[] arrayxml = {};
                    paintEvent.gc.setLineWidth(5);
                    paintEvent.gc.drawPolyline(arrayxml);
                    log.info("Graph with info from Xml Provider have been drawn");
                } else if (providerId == 1) {
                    int[] arrayjson = {};
                    paintEvent.gc.setLineWidth(5);
                    paintEvent.gc.drawPolyline(arrayjson);
                    log.info("Graph with info from Json Provider have been drawn");
                }
            }
        }
        );
    shell.setSize(550,520);
}

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    }
}
