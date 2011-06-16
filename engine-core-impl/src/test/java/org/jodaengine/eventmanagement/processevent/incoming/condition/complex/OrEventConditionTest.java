package org.jodaengine.eventmanagement.processevent.incoming.condition.complex;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.complex.OrEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.FalseEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link OrEventCondition}.
 */
public class OrEventConditionTest {

    private OrEventCondition orEventCondition;
    private AdapterEvent adapterEventMock;

    /**
     * Setting up all necessary objects and mocks.
     */
    @BeforeMethod
    public void setUp() {

        orEventCondition = new OrEventCondition();
        adapterEventMock = Mockito.mock(AdapterEvent.class);
    }

    /**
     * Tests whether the {@link OrEventCondition} evaluates three {@link TrueEventCondition}s to true.
     */
    @Test
    public void testAndEventConditionWithAllTrue() {

        orEventCondition.addEventCondition(new TrueEventCondition()).addEventCondition(new TrueEventCondition())
        .addEventCondition(new TrueEventCondition());
        Assert.assertTrue(orEventCondition.evaluate(adapterEventMock));
    }

    /**
     * Tests whether the {@link OrEventCondition} evaluates two {@link TrueEventCondition}s and one
     * {@link FalseEventCondition} to true.
     */
    @Test
    public void testAndEventConditionWithOneFalse() {

        orEventCondition.addEventCondition(new TrueEventCondition()).addEventCondition(new FalseEventCondition())
        .addEventCondition(new TrueEventCondition());

        Assert.assertTrue(orEventCondition.evaluate(adapterEventMock));
    }

    /**
     * Tests whether the {@link OrEventCondition} evaluates two {@link TrueEventCondition}s and one
     * {@link FalseEventCondition} to true. But in this case the {@link FalseEventCondition} is the first condition.
     * This methods tests a faster implementation of {@link OrEventCondition#evaluate(AdapterEvent)}.
     */
    @Test
    public void testAndEventConditionWithOneFalseAtFirst() {

        orEventCondition.addEventCondition(new FalseEventCondition()).addEventCondition(new TrueEventCondition())
        .addEventCondition(new TrueEventCondition());

        Assert.assertTrue(orEventCondition.evaluate(adapterEventMock));
    }

    /**
     * Tests whether the {@link OrEventCondition} evaluates three {@link FalseEventCondition}s to false.
     */
    @Test
    public void testAndEventConditionWithAllFalse() {

        orEventCondition.addEventCondition(new FalseEventCondition()).addEventCondition(new FalseEventCondition())
        .addEventCondition(new FalseEventCondition());

        Assert.assertFalse(orEventCondition.evaluate(adapterEventMock));
    }
}
