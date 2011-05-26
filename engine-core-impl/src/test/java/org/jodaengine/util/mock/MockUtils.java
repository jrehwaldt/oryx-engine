package org.jodaengine.util.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;

/**
 * The Class MockUtils has methods to create more complex mocks that are reused quite often.
 */
public final class MockUtils {

    /**
     * Hidden constructor.
     */
    private MockUtils() { }
    
    /**
     * Creates a mock token, that is linked to a mock ProcessInstance and a mock ProcessDefinition.
     *
     * @return the token
     */
    public static Token fullyMockedToken() {
        Token token = mock(Token.class);
        AbstractProcessInstance instance = mockProcessInstance();        
        when(token.getInstance()).thenReturn(instance);        
        
        return token;
    }
    
    /**
     * Mocks a process definition and returns a random id for getID().
     *
     * @return the process definition
     */
    public static ProcessDefinition mockProcessDefinition() {
        ProcessDefinition definition = mock(ProcessDefinition.class);
        when(definition.getID()).thenReturn(new ProcessDefinitionID(UUID.randomUUID()));
        return definition;
    }
    
    /**
     * Mocks process instance with a definition.
     *
     * @return the abstract process instance
     */
    public static AbstractProcessInstance mockProcessInstance() {
        AbstractProcessInstance instance = mock(AbstractProcessInstance.class);
        ProcessDefinition definition = mockProcessDefinition();      
        when(instance.getDefinition()).thenReturn(definition);
        return instance;
    }
}
