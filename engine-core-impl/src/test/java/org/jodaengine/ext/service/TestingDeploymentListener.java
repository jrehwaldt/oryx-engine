package org.jodaengine.ext.service;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.WorklistService;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.Extension;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.xml.XmlElement;

/**
 * This implementation tests the availability of a {@link BpmnXmlParseListener} implementation
 * via the {@link ExtensionService} and the proper calling of the constructor.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Extension(TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME)
public class TestingDeploymentListener implements BpmnXmlParseListener {
    
    protected JodaEngineServices services;
    protected Navigator navigator;
    protected WorklistService worklist;
    protected ExtensionService extension;
    protected RepositoryService repository;
    protected TestingExtensionService testing;
    
    /**
     * Testing constructor.
     * 
     * @param services any service
     * @param navigator any service
     * @param worklist any service
     * @param extension any service
     * @param repository any service
     * @param testing any service
     */
    public TestingDeploymentListener(JodaEngineServices services,
                                     Navigator navigator,
                                     WorklistService worklist,
                                     ExtensionService extension,
                                     RepositoryService repository,
                                     TestingExtensionService testing) {

        this.services = services;
        this.navigator = navigator;
        this.worklist = worklist;
        this.extension = extension;
        this.repository = repository;
        this.testing = testing;
    }

    @Override
    public void parseProcess(XmlElement processXmlElement, ProcessDefinition processDefinition) {
        // do nothing
    }

    @Override
    public void parseStartEvent(XmlElement startEventXmlElement, Node startNode) {
        // do nothing
    }

    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement, Node exclusiveGatewayNode) {
        // do nothing
    }

    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement, Node parallelGatewayNode) {
        // do nothing
    }

    @Override
    public void parseTask(XmlElement taskXmlElement, Node taskNode) {
        // do nothing
    }

    @Override
    public void parseUserTask(XmlElement userTaskXmlElement, Node userTaskNode) {
        // do nothing
    }

    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt, Node endEventNode) {
        // do nothing
    }

    @Override
    public void parseSequenceFlow(XmlElement sequenceFlowElement, Transition transition) {
        // do nothing
    }
}
