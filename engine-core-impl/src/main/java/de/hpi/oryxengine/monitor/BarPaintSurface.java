package de.hpi.oryxengine.monitor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class PaintSurface.
 */
public class BarPaintSurface extends PaintSurface {

    private int runningInstances;
    private int maximumInstances;
    
    //The delta of process instances being removed/added to the scheduler in order to trigger a GUI update.
    private int REPAINT_TOLERANCE = 200;
    private int NUMBER_OF_BARS = 15;

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

        g.drawString("ProcessInstances in Queue: " + new Integer(runningInstances).toString() + "/" + new Integer(maximumInstances).toString(), 10, 10);
    }

    private void paintSingleBar(Graphics g, int x, int y) {

        int barHeight = (int) (((double) height) * 0.9);
        double barWidthAsDouble = ((double) width * 0.9) / NUMBER_OF_BARS;
        int barWidth = (int) barWidthAsDouble;
        g.setColor(Color.GREEN);
        g.fillRect(x, y, barWidth, barHeight);
    }

    private int numberOfBars() {

        // we are calculating with int, so order of operations is important
        if ((runningInstances * NUMBER_OF_BARS) % maximumInstances != 0) {
            return ((runningInstances * NUMBER_OF_BARS) / maximumInstances) + 1;
        }
        return (runningInstances * NUMBER_OF_BARS) / maximumInstances;
    }

    private void paintBackground(Graphics g) {

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);
    }

    public int getRunningInstances() {

        return runningInstances;
    }

    public void setRunningInstances(int runningInstances) {

        this.runningInstances = runningInstances;
    }

    public int getMaximumInstances() {

        return maximumInstances;
    }

    public void setMaximumInstances(int maximumInstances) {

        this.maximumInstances = maximumInstances;
    }

    public void updateInstances(int runningInstances) {

        // only update and repaint, if the difference is at least beyond a given repaint-tolerance.
        if (Math.abs(this.runningInstances - runningInstances) > REPAINT_TOLERANCE || runningInstances == 0) {
            this.runningInstances = runningInstances;
            this.repaint();
        }
    }

}
