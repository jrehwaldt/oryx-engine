package org.jodaengine.ext.listener.deployment;

import static org.mockito.Mockito.mock;

import java.io.InputStream;

import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.jodaengine.util.xml.XmlElement;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the injection of {@link BpmnXmlParseListener}s.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class BpmnXmlParseListenerTest extends AbstractJodaEngineTest {
    
    private static final short NUMBER_OF_SEQUENCE_FLOWS = 11;
    private static final short NUMBER_OF_TASKS = 4;
    
    
    private static final String USER_PROCESS_DEFINITION_FILE
        = "org/jodaengine/ext/listener/deployment/UserTaskBpmnListenerTest.bpmn.xml";
    
    private static final String GATEWAY_PROCESS_DEFINITION_FILE
        = "org/jodaengine/ext/listener/deployment/GatewaysBpmnListenerTest.bpmn.xml";
    
    private BpmnXmlParseListener listener;
    
    /**
     * Setup a fresh mock {@link BpmnXmlParseListener}.
     */
    @BeforeMethod
    public void setUp() {
        this.listener = mock(BpmnXmlParseListener.class);
        
    }
    
    /**
     * Tests the calling of the specified listener methods.
     */
    @Test
    public void testListenerMethodCallsForUserTaskProcess() {
        
        //
        // we need Thorben to help us with the process
        //
        ServiceFactory.getIdentityService().getIdentityBuilder().createParticipant("Thorben");
        
        //
        // okay, let's rock
        //
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(USER_PROCESS_DEFINITION_FILE);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        ArgumentCaptor<XmlElement> xmlElementArgument = ArgumentCaptor.forClass(XmlElement.class);
        ArgumentCaptor<Node> nodeArgument = ArgumentCaptor.forClass(Node.class);
        ArgumentCaptor<ProcessDefinition> processDefinitionArgument = ArgumentCaptor.forClass(ProcessDefinition.class);
        ArgumentCaptor<Transition> transitionArgument = ArgumentCaptor.forClass(Transition.class);
        
        //
        // parseEndEvent
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseEndEvent(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseProcess
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseProcess(
            xmlElementArgument.capture(),
            processDefinitionArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(processDefinitionArgument.getValue());
        
        //
        // parseStartEvent
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseStartEvent(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseSequenceFlow
        //
        Mockito.verify(this.listener, Mockito.times(2)).parseSequenceFlow(
            xmlElementArgument.capture(),
            transitionArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(transitionArgument.getValue());
        
        //
        // parseUserTask
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseUserTask(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
    }
    
    /**
     * Tests the calling of the specified listener methods.
     */
    @Test
    public void testListenerMethodCallsForGatewayProcess() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(GATEWAY_PROCESS_DEFINITION_FILE);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        ArgumentCaptor<XmlElement> xmlElementArgument = ArgumentCaptor.forClass(XmlElement.class);
        ArgumentCaptor<Node> nodeArgument = ArgumentCaptor.forClass(Node.class);
        ArgumentCaptor<ProcessDefinition> processDefinitionArgument = ArgumentCaptor.forClass(ProcessDefinition.class);
        ArgumentCaptor<Transition> transitionArgument = ArgumentCaptor.forClass(Transition.class);
        
        //
        // parseEndEvent
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseEndEvent(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseProcess
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseProcess(
            xmlElementArgument.capture(),
            processDefinitionArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(processDefinitionArgument.getValue());
        
        //
        // parseStartEvent
        //
        Mockito.verify(this.listener, Mockito.times(1)).parseStartEvent(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseSequenceFlow
        //
        Mockito.verify(this.listener, Mockito.times(NUMBER_OF_SEQUENCE_FLOWS)).parseSequenceFlow(
            xmlElementArgument.capture(),
            transitionArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(transitionArgument.getValue());
        
        //
        // parseTask
        //
        Mockito.verify(this.listener, Mockito.times(NUMBER_OF_TASKS)).parseTask(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseExclusiveGateway
        //
        Mockito.verify(this.listener, Mockito.times(2)).parseExclusiveGateway(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
        
        //
        // parseParallelGateway
        //
        Mockito.verify(this.listener, Mockito.times(2)).parseParallelGateway(
            xmlElementArgument.capture(),
            nodeArgument.capture());
        
        Assert.assertNotNull(xmlElementArgument.getValue());
        Assert.assertNotNull(nodeArgument.getValue());
    }
}
