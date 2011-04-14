package de.hpi.oryxengine.loadgenerator;

import org.testng.annotations.Test;

/**
 * Simply creates a loadgenerator with the standard parameters but changes the number of instances to 1 since we don't
 * want the tests to take forever.
 */
public class LoadGeneratorTest {

    /**
     * Test that the load generator works, by starting just one instance of the default process.
     */
    @Test
    public void testLoadGeneratorWorks() {

        LoadGenerator gene = new LoadGenerator(1);
        gene.execute();
    }
}
