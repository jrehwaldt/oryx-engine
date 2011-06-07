package org.jodaengine.ext.service;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.WorklistService;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.Extension;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.xml.XmlElement;
import org.testng.Assert;

/**
 * This implementation tests the availability of a {@link BpmnXmlParseListener} implementation
 * via the {@link ExtensionService} and the proper calling of the constructor.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Extension(TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME + "-bpmn-listener")
public class TestingBpmnXmlParseListener implements BpmnXmlParseListener {
    
    protected JodaEngineServices services;
    protected Navigator navigator;
    protected WorklistService worklist;
    protected ExtensionService extension;
    protected RepositoryService repository;
    protected TestingExtensionService testing;
    protected TestingListenerExtensionService listenerService;
    
    /**
     * Testing constructor.
     * 
     * @param services any service
     * @param navigator any service
     * @param worklist any service
     * @param extension any service
     * @param repository any service
     * @param testing any service
     * @param listenerService the listener service
     */
    public TestingBpmnXmlParseListener(JodaEngineServices services,
                                       Navigator navigator,
                                       WorklistService worklist,
                                       ExtensionService extension,
                                       RepositoryService repository,
                                       TestingExtensionService testing,
                                       TestingListenerExtensionService listenerService) {

        this.services = services;
        this.navigator = navigator;
        this.worklist = worklist;
        this.extension = extension;
        this.repository = repository;
        this.testing = testing;
        
        Assert.assertNotNull(listenerService);
        this.listenerService = listenerService;
        
        this.listenerService.registered(this);
    }

    @Override
    public void parseProcess(XmlElement processXmlElement, ProcessDefinition processDefinition) {
        listenerService.invoked(this);
    }

    @Override
    public void parseStartEvent(XmlElement startEventXmlElement,
                                Node startNode,
                                Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement,
                                      Node exclusiveGatewayNode,
                                      Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement,
                                     Node parallelGatewayNode,
                                     Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseTask(XmlElement taskXmlElement,
                          Node taskNode,
                          Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseUserTask(XmlElement userTaskXmlElement,
                              Node userTaskNode,
                              Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt,
                              Node endEventNode,
                              Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }

    @Override
    public void parseSequenceFlow(XmlElement sequenceFlowElement,
                                  ControlFlow controlFlow,
                                  Attributable definitionScopeAttributable) {
        
        this.listenerService.invoked(this);
    }
}
