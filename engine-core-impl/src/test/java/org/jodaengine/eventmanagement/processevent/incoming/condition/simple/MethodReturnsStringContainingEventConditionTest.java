package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;

import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Testing the class MethodReturnsStringContainingEventCondition,
 * it should return true as long as the String returned by the specified
 * method returns the specified string.
 */
public class MethodReturnsStringContainingEventConditionTest {

    private static final String ID = "ID";
    private MailAdapterEvent event;
    
    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {
        event = Mockito.mock(MailAdapterEvent.class);
        Mockito.when(event.getMessageSubject()).thenReturn("Re: " + ID);
    }
    
    /**
     * Test re mail true.
     */
    @Test
    public void testReMailTrue() {
        EventCondition condition = new MethodReturnsStringContainingEventCondition(
            MailAdapterEvent.class, 
            "getMessageSubject", 
            ID);
        Assert.assertTrue(condition.evaluate(event));
    }
    
    /**
     * Test re mail false.
     */
    @Test
    public void testReMailFalse() {
        EventCondition condition = new MethodReturnsStringContainingEventCondition(
            MailAdapterEvent.class, 
            "getMessageSubject", 
            "somethingeElse");
        Assert.assertFalse(condition.evaluate(event));
    }
}
