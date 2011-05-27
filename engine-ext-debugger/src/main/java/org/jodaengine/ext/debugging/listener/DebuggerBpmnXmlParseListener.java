package org.jodaengine.ext.debugging.listener;

import javax.annotation.Nonnull;

import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParser;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.xml.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process import
 * using the {@link BpmnXmlParser} takes place and can do further processing of debugger extensions,
 * if those exist.
 * 
 * When invoked the listener searches for extension elements in the xml serialization, which belong to our
 * debugger namespace. In case those are found the data are transfered to the {@link Node}'s attribute set
 * in order to be available in the {@link org.jodaengine.process.instance.AbstractProcessInstance}'s lifecycle.
 * 
 * Those extension points are defined in the BPMN 2.0 specification as of chapter 8.2.3.
 * 
 * The debugger extension namespace is defined in the constant <code>JODAENGINE_EXTENSION_DEBUGGER_NS</code>.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerBpmnXmlParseListener implements BpmnXmlParseListener {

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
        
        logger.debug("Parse BPMN-process element {}", processXmlElement);
        
        //
        // enable debug mode via xml definition
        //
        String debugMode = processXmlElement.getAttributeNS(JODAENGINE_EXTENSION_DEBUGGER_NS, "debug-mode");
        
        if (Boolean.valueOf(debugMode)) {
            logger.info("Enable debugging for {}.", processDefinition);
            DebuggerAttribute attribute = DebuggerAttribute.getAttribute(processDefinition);
            attribute.enable();
        }
    }
    
    @Override
    public void parseTask(XmlElement taskXmlElement,
                          Node taskNode) {
        
        logger.debug("Parse BPMN-task element {}", taskNode);
        parseElement(taskXmlElement, taskNode, BpmnConstructType.ACTIVITY);
    }
    
    @Override
    public void parseUserTask(XmlElement userTaskXmlElement,
                              Node userTaskNode) {
        
        logger.debug("Parse BPMN-user-task element {}", userTaskNode);
        parseElement(userTaskXmlElement, userTaskNode, BpmnConstructType.ACTIVITY);
    }
    
    @Override
    public void parseSequenceFlow(XmlElement sequenceFlowElement,
                                  Transition transition) {

        logger.debug("Parse BPMN-sequence element {}", transition);
        parseSequenceElement(sequenceFlowElement, transition);
    }

    @Override
    public void parseStartEvent(XmlElement startEventXmlElement,
                                Node startNode) {
        
        logger.debug("Parse BPMN-start-event element {}", startNode);
        parseElement(startEventXmlElement, startNode, BpmnConstructType.EVENT);
    }
    
    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt,
                              Node endEventNode) {
        
        logger.debug("Parse BPMN-end element {}", endEventNode);
        parseElement(endEventXmlElemnt, endEventNode, BpmnConstructType.EVENT);
    }
    
    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement,
                                      Node exclusiveGatewayNode) {
        
        logger.debug("Parse BPMN-exclusive-gateway element {}", exclusiveGatewayNode);
        parseElement(exclusiveGatewayXmlElement, exclusiveGatewayNode, BpmnConstructType.GATEWAY);
    }
    
    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement,
                                     Node parallelGatewayNode) {
        
        logger.debug("Parse BPMN-parallel-gateway element {}", parallelGatewayNode);
        parseElement(parallelGatewayXmlElement, parallelGatewayNode, BpmnConstructType.GATEWAY);
    }
    
    /**
     * Parse any constructed element except {@link Transition}s.
     * 
     * @param xmlElement the {@link XmlElement} representation
     * @param node the parsed and constructed {@link Node} representation
     * @param type the element's control flow construct type
     */
    private void parseElement(@Nonnull XmlElement xmlElement,
                              @Nonnull Node node,
                              @Nonnull BpmnConstructType type) {
        
        
    }
    
    /**
     * Parse any constructed {@link Transition}.
     * 
     * @param xmlElement the {@link XmlElement} representation
     * @param transition the parsed and constructed {@link Transition} representation
     */
    private void parseSequenceElement(@Nonnull XmlElement xmlElement,
                                      @Nonnull Transition transition) {
        
        
    }
    
    /**
     * Any available bpmn control flow construct.
     * 
     * @author Jan Eehwaldt
     * @since 2011-05-24
     */
    private enum BpmnConstructType {
        GATEWAY,
        ACTIVITY,
        EVENT,
        SEQUENCE
    }
}
