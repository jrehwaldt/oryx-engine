package de.hpi.oryxengine.monitor;

import java.awt.Color;
import java.awt.Graphics;

/**
 * The Class PaintSurface.
 */
public class BarPaintSurface extends PaintSurface {

    private int runningInstances;
    private int maximumInstances;

    // The delta of process instances being removed/added to the scheduler in order to trigger a GUI update.
    private final static int REPAINT_TOLERANCE = 200;
    private final static int NUMBER_OF_BARS = 15;

    /**
     * Instantiates a new bar paint surface.
     *
     * @param maximumInstances the maximum instances
     */
    public BarPaintSurface(int maximumInstances) {

        super();
        this.runningInstances = 0;
        this.maximumInstances = maximumInstances;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        double overallBarWidth = ((double) width) / NUMBER_OF_BARS;
        int overallBarYOffset = (int) (height * 0.05);
        for (int i = 0; i < numberOfBars(); i++) {

            paintSingleBar(g, (int) (i * overallBarWidth), overallBarYOffset);
        }

        g.setColor(Color.BLACK);

        g.drawString("ProcessInstances in Queue: " + new Integer(runningInstances).toString() + "/"
            + new Integer(maximumInstances).toString(), 10, 10);
    }

    /**
     * Paint a single bar.
     * 
     * @param g
     *            the Graphics object that paints everything.
     * @param x
     *            the x
     * @param y
     *            the y
     */
    private void paintSingleBar(Graphics g, int x, int y) {

        int barHeight = (int) (((double) height) * 0.9);
        double barWidthAsDouble = ((double) width * 0.9) / NUMBER_OF_BARS;
        int barWidth = (int) barWidthAsDouble;
        g.setColor(Color.GREEN);
        g.fillRect(x, y, barWidth, barHeight);
    }

    /**
     * The number of bars that have to be drawn in this particular drawing step.
     * 
     * @return the int
     */
    private int numberOfBars() {

        // we are calculating with int, so order of operations is important
        if ((runningInstances * NUMBER_OF_BARS) % maximumInstances != 0) {
            return ((runningInstances * NUMBER_OF_BARS) / maximumInstances) + 1;
        }
        return (runningInstances * NUMBER_OF_BARS) / maximumInstances;
    }

    /**
     * Gets the running instances.
     * 
     * @return the running instances
     */
    public int getRunningInstances() {

        return runningInstances;
    }

    /**
     * Sets the running instances.
     * 
     * @param runningInstances
     *            the new running instances
     */
    public void setRunningInstances(int runningInstances) {

        this.runningInstances = runningInstances;
    }

    /**
     * Gets the maximum instances.
     * 
     * @return the maximum instances
     */
    public int getMaximumInstances() {

        return maximumInstances;
    }

    /**
     * Sets the maximum instances.
     * 
     * @param maximumInstances
     *            the new maximum instances
     */
    public void setMaximumInstances(int maximumInstances) {

        this.maximumInstances = maximumInstances;
    }

    /**
     * Updates instances. Only repaints the Panel, if the delta of old instances and new instances is greater than the
     * defined one.
     * 
     * @param runningInstances
     *            the running instances
     */
    public void updateInstances(int runningInstances) {

        // only update and repaint, if the difference is at least beyond a given repaint-tolerance.
        if (Math.abs(this.runningInstances - runningInstances) > REPAINT_TOLERANCE || runningInstances == 0) {
            this.runningInstances = runningInstances;
            this.repaint();
        }
    }

}
