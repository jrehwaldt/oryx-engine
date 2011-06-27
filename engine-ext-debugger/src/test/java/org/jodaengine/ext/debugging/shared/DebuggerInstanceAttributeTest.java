package org.jodaengine.ext.debugging.shared;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.jodaengine.ext.debugging.util.DebuggerInstanceAttributeKeyProvider;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the {@link DebuggerInstanceAttribute} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public class DebuggerInstanceAttributeTest {
    
    private static final short[] TEST_HISTORY = {4, 2, 1, 2};
    
    /**
     * Tests that the path history is correctly stored.
     */
    @Test
    public void testStoringOfPathHistory() {
        
        Token mockTokenEven = mock(Token.class);
        Token mockTokenOdd = mock(Token.class);
        when(mockTokenEven.getID()).thenReturn(UUID.randomUUID());
        when(mockTokenOdd.getID()).thenReturn(UUID.randomUUID());
        
        DebuggerInstanceAttribute attribute = new DebuggerInstanceAttribute();
        
        //
        // store the history
        //
        for (short j = 0; j < TEST_HISTORY.length; j++) {
            
            for (short i = 0; i < TEST_HISTORY[j]; i++) {
                if (j % 2 == 0) {
                    attribute.addPreviousPath(mock(ControlFlow.class), mockTokenEven);
                } else {
                    attribute.addPreviousPath(mock(ControlFlow.class), mockTokenOdd);
                }
            }
        }
        
        //
        // get the history
        //
        List<PathHistoryEntry> history = attribute.getFullPath();
        Assert.assertNotNull(history);
        Assert.assertFalse(history.isEmpty());
        
        //
        // is the order correct
        //
        for (short j = 0; j < TEST_HISTORY.length; j++) {
            
            for (short i = 0; i < TEST_HISTORY[j]; i++) {
                PathHistoryEntry entry = history.get(i);
                if (j % 2 == 0) {
                    entry.getTokenID().equals(mockTokenEven.getID());
                } else {
                    entry.getTokenID().equals(mockTokenOdd.getID());
                }
            }
        }
    }
    
    /**
     * Tests that a valid id is specified.
     */
    @Test
    public void testIdNotNull() {
        DebuggerInstanceAttribute attribute = new DebuggerInstanceAttribute();
        Assert.assertNotNull(attribute.getID());
    }
    
    /**
     * Tests the creation and binding of a {@link DebuggerInstanceAttribute}
     * to a {@link Token}.
     */
    @Test
    public void testCreationAndBindingOfAnAttribute() {
        Token mockToken = mock(Token.class);
        
        DebuggerInstanceAttribute attribute = DebuggerInstanceAttribute.getAttribute(mockToken);
        Assert.assertNotNull(attribute);
        
        ArgumentCaptor<DebuggerAttribute> attributeCap = ArgumentCaptor.forClass(DebuggerAttribute.class);
        
        verify(mockToken).setAttribute(
            eq(DebuggerInstanceAttributeKeyProvider.getAttributeKey()),
            attributeCap.capture());
        Assert.assertNotNull(attributeCap.getValue());
        Assert.assertEquals(attributeCap.getValue(), attribute);
    }
    
//    /**
//     * Tests the creation and binding of a {@link DebuggerInstanceAttribute}
//     * to a {@link Token}.
//     */
//    @Test
//    public void testCreationAndBindingOfAnAttributeOverParentToken() {
//        Token mockParentToken = mock(Token.class);
//        Token mockChildToken = mock(Token.class);
//        when(mockParentToken.getParentToken()).thenReturn(null);
//        when(mockChildToken.getParentToken()).thenReturn(mockParentToken);
//        
//        DebuggerInstanceAttribute childAttribute = DebuggerInstanceAttribute.getAttribute(mockChildToken);
//        Assert.assertNotNull(childAttribute);
//        
//        ArgumentCaptor<DebuggerAttribute> parentAttributeCap = ArgumentCaptor.forClass(DebuggerAttribute.class);
//        
//        verify(mockParentToken).setAttribute(
//            eq(DebuggerInstanceAttributeKeyProvider.getAttributeKey()),
//            parentAttributeCap.capture());
//        Assert.assertNotNull(parentAttributeCap.getValue());
//        Assert.assertEquals(parentAttributeCap.getValue(), childAttribute);
//        
//        verify(mockChildToken, never()).setAttribute(
//            eq(DebuggerInstanceAttributeKeyProvider.getAttributeKey()), any());
//    }
    
//    /**
//     * Tests that an attribute is correctly provided even if it is registered within a parent token.
//     */
//    @Test
//    public void testGettingAnAttributeFromChild() {
//        
//        DebuggerInstanceAttribute mockAttribute = mock(DebuggerInstanceAttribute.class);
//        Token mockParentToken = mock(Token.class);
//        Token mockChildToken = mock(Token.class);
//        when(mockParentToken.getParentToken()).thenReturn(null);
//        when(mockChildToken.getParentToken()).thenReturn(mockParentToken);
//        when(mockParentToken.getAttribute(
//            DebuggerInstanceAttributeKeyProvider.getAttributeKey())).thenReturn(mockAttribute);
//        
//        DebuggerInstanceAttribute attribute = DebuggerInstanceAttribute.getAttribute(mockChildToken);
//        Assert.assertNotNull(attribute);
//        
//        Assert.assertTrue(mockAttribute.equals(attribute));
//    }
    
//  /**
//   * When a token state changes a parent {@link Token} may have a {@link DebuggerCommand} attached.
//   * 
//   * This is tested here.
//   */
//  @Test
//  public void testRecognitionOfParentTokenCommand() {
//      
//      Token mockParentToken = mock(Token.class);
//      Token mockChildToken = mock(Token.class);
//      Token mockAnotherChildToken = mock(Token.class);
//      
//      when(mockParentToken.getParentToken()).thenReturn(null);
//      when(mockChildToken.getParentToken()).thenReturn(mockParentToken);
//      when(mockAnotherChildToken.getParentToken()).thenReturn(mockParentToken);
//      
//      when(mockParentToken.getID()).thenReturn(UUID.randomUUID());
//      when(mockChildToken.getID()).thenReturn(UUID.randomUUID());
//      when(mockAnotherChildToken.getID()).thenReturn(UUID.randomUUID());
//      
//      DebuggerInstanceAttribute attribute = new DebuggerInstanceAttribute();
//      
//      //
//      // no command registered
//      //
//      Assert.assertNull(attribute.getCommand(mockAnotherChildToken));
//      
//      //
//      // registered command is returned
//      //
//      attribute.setCommand(mockChildToken, DebuggerCommand.CONTINUE);
//      Assert.assertTrue(DebuggerCommand.CONTINUE.equals(attribute.getCommand(mockChildToken)));
//      
//      //
//      // command registered (inherited from parent)
//      //
//      Assert.assertTrue(DebuggerCommand.CONTINUE.equals(attribute.getCommand(mockAnotherChildToken)));
//      
//      //
//      // register for parent
//      // -> it should be available for child if own command is missing
//      //
//      attribute.setCommand(mockParentToken, DebuggerCommand.RESUME);
//      Assert.assertTrue(DebuggerCommand.RESUME.equals(attribute.getCommand(mockAnotherChildToken)));
//      
//      //
//      // -> it should NOT be available for child if own command is available
//      //
//      Assert.assertTrue(DebuggerCommand.CONTINUE.equals(attribute.getCommand(mockChildToken)));
//  }
}
