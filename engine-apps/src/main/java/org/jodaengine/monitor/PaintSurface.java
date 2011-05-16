package org.jodaengine.monitor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The Class PaintSurface.
 */
public class PaintSurface extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -714543702943220898L;
    protected int width, height;

    /**
     * Instantiates a new paint surface.
     */
    public PaintSurface() {

        super();
        this.width = 0;
        this.height = 0;
    }

    /**
     * Checks if is opaque.
     * 
     * @return true, if is opaque {@inheritDoc}
     */
    @Override
    public boolean isOpaque() {

        return true;
    }

    /**
     * Paint component.
     * 
     * @param g
     *            the g {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {

        width = getWidth();
        height = getHeight();

        paintBackground(g);
    }

    /**
     * Paint background.
     * 
     * @param g
     *            the g
     */
    private void paintBackground(Graphics g) {

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);
    }
}
