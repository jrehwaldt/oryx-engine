package org.jodaengine.ext.debugging.listener;

import org.jodaengine.deployment.definition.importer.bpmn.BpmnXmlParseListener;
import org.jodaengine.deployment.definition.importer.bpmn.BpmnXmlParser;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.xml.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process import
 * takes place {@link BpmnXmlParser} and can do further processing of debugger extensions, if exists.
 * 
 * When invoked the listener searches for extension elements in the xml serialization, which belong to our
 * debugger namespace. In case those are found the data are transfered to the {@link Node}'s attribute set
 * in order to be available in the {@link AbstractProcessInstance}'s lifecycle.
 * 
 * Those extension points are defined in the BPMN 2.0 specification as of chapter 8.2.3.
 * 
 * The debugger extension namespace is defined in the constant <code>JODAENGINE_EXTENSION_DEBUGGER_NS</code>.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
public class DebuggerDeploymentImportListener implements BpmnXmlParseListener {
    
    /**
     * The debugger extension namespace.
     * 
     * It can be found in xmlns:<i>debugger</i>="<code>JODAENGINE_EXTENSION_DEBUGGER_NS</code>", whereas
     * the xml intern namespace mapping (here: debugger) is not fixed and can vary between several xml files.
     * 
     * <definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *        xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
     *        xmlns:debugger="http://jodaengine.org/bpmn-extensions/debugger"
     *        xsi:schemaLocation="http://schema.omg.org/spec/BPMN/2.0 BPMN20.xsd"
     *        typeLanguage="http://www.w3.org/2001/XMLSchema"
     *        expressionLanguage="http://www.w3.org/1999/XPath">
     *        
     *     <!-- BPMN processes go here -->
     *     
     * </definitions>
     */
    public static final String JODAENGINE_EXTENSION_DEBUGGER_NS = BpmnXmlParser.JODAENGINE_EXTENSIONS_NS + "/debugger";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    //=================================================================
    //=================== Parse Listener methods ======================
    //=================================================================
    
    @Override
    public void parseProcess(XmlElement processXmlElement,
                             ProcessDefinition processDefinition) {
        
        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-process element {}", processXmlElement);
    }
    
    @Override
    public void parseTask(XmlElement taskXmlElement,
                          Node taskNode) {
        
        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-task element {}", taskNode);
    }
    
    @Override
    public void parseUserTask(XmlElement userTaskXmlElement,
                              Node userTaskNode) {
        
        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-user-task element {}", userTaskNode);
    }
    
    @Override
    public void parseSequenceFlow(XmlElement sequenceFlowElement,
                                  Transition transition) {

        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-sequence element {}", transition);
    }
    
    @Override
    public void parseStartEvent(XmlElement startEventXmlElement,
                                Node startNode) {
        
        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-start-event element {}", startNode);
    }
    
    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt,
                              Node endEventNode) {
        
        // TODO Auto-generated method stub
        logger.debug("Parse BPMN-end element {}", endEventNode);
    }
    
    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement,
                                      Node exclusiveGatewayNode) {
        
        logger.debug("Skip BPMN-exclusive-gateway element {}", exclusiveGatewayNode);
    }
    
    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement,
                                     Node parallelGatewayNode) {
        
        logger.debug("Skip BPMN-parallel-gateway element {}", parallelGatewayNode);
    }
}
