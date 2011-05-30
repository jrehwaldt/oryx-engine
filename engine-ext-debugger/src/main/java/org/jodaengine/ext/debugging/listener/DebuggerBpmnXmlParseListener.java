package org.jodaengine.ext.debugging.listener;

import javax.annotation.Nonnull;

import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParser;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.Attributable;
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
        // get the extension element and create a DebuggerAttribute instance in the process context
        //
        XmlElement extensionElement = processXmlElement.getElement(BPMN_EXTENSIONS_ELEMENT_NAME);
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(processDefinition);
        
        if (extensionElement == null) {
            return;
        }
        
        //
        // enable debug mode via xml definition
        //
        XmlElement debugModeElement = extensionElement.getElementNS(JODAENGINE_EXTENSION_DEBUGGER_NS, "enabled");
        
        if (debugModeElement != null && Boolean.valueOf(debugModeElement.getText())) {
            logger.info("Enable debugging for {}.", processDefinition);
            attribute.enable();
        }
        
        //
        // get the svg artifact resource name, either from
        //    a) svg-artifact attribute
        // or
        //    b) process definition id
        //
        XmlElement svgArtifactElement = extensionElement.getElementNS(JODAENGINE_EXTENSION_DEBUGGER_NS, "svg-artifact");
        if (svgArtifactElement != null) {
            attribute.setSvgArtifact(svgArtifactElement.getText());
        } else {
            attribute.setSvgArtifact(String.format("%s.svg", processDefinition.getID().getIdentifier()));
        }
    }
    
    @Override
    public void parseTask(XmlElement taskXmlElement,
                          Node taskNode,
                          Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-task element {}", taskNode);
        parseElement(
            taskXmlElement,
            taskNode,
            definitionScopeAttributable,
            BpmnConstructType.ACTIVITY);
    }
    
    @Override
    public void parseUserTask(XmlElement userTaskXmlElement,
                              Node userTaskNode,
                              Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-user-task element {}", userTaskNode);
        parseElement(
            userTaskXmlElement,
            userTaskNode,
            definitionScopeAttributable,
            BpmnConstructType.ACTIVITY);
    }
    
    @Override
    public void parseSequenceFlow(XmlElement sequenceFlowElement,
                                  Transition transition,
                                  Attributable definitionScopeAttributable) {

        logger.debug("Parse BPMN-sequence element {}", transition);
        parseSequenceElement(
            sequenceFlowElement,
            transition,
            definitionScopeAttributable);
    }

    @Override
    public void parseStartEvent(XmlElement startEventXmlElement,
                                Node startNode,
                                Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-start-event element {}", startNode);
        parseElement(
            startEventXmlElement,
            startNode,
            definitionScopeAttributable,
            BpmnConstructType.EVENT);
    }
    
    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt,
                              Node endEventNode,
                              Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-end element {}", endEventNode);
        parseElement(
            endEventXmlElemnt,
            endEventNode,
            definitionScopeAttributable,
            BpmnConstructType.EVENT);
    }
    
    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement,
                                      Node exclusiveGatewayNode,
                                      Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-exclusive-gateway element {}", exclusiveGatewayNode);
        parseElement(
            exclusiveGatewayXmlElement,
            exclusiveGatewayNode,
            definitionScopeAttributable,
            BpmnConstructType.GATEWAY);
    }
    
    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement,
                                     Node parallelGatewayNode,
                                     Attributable definitionScopeAttributable) {
        
        logger.debug("Parse BPMN-parallel-gateway element {}", parallelGatewayNode);
        parseElement(
            parallelGatewayXmlElement,
            parallelGatewayNode,
            definitionScopeAttributable,
            BpmnConstructType.GATEWAY);
    }
    
    /**
     * Parse any constructed element except {@link Transition}s.
     * 
     * @param xmlElement the {@link XmlElement} representation
     * @param node the parsed and constructed {@link Node} representation
     * @param definitionAttributes a definition-scoped {@link Attributable}
     * @param type the element's control flow construct type
     */
    private void parseElement(@Nonnull XmlElement xmlElement,
                              @Nonnull Node node,
                              @Nonnull Attributable definitionAttributes,
                              @Nonnull BpmnConstructType type) {
        
        //
        // is debugging enabled?
        //
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(definitionAttributes);
        // TODO @Gerardo(CodeReview) Warum ist das hier einkommentiert?
//        if (!attribute.isEnabled()) {
//            return;
//        }

        //
        // register breakpoint, if available
        //
        Breakpoint breakpoint = parseBreakpoint(xmlElement, node);
        if (breakpoint != null) {
            attribute.addBreakpoint(breakpoint);
            logger.info("Breakpoint {} for {} registered.", breakpoint, node);
        }
    }
    
    /**
     * Parse any constructed {@link Transition}.
     * 
     * @param xmlElement the {@link XmlElement} representation
     * @param transition the parsed and constructed {@link Transition} representation
     * @param definitionAttributes a definition-scoped {@link Attributable}
     */
    private void parseSequenceElement(@Nonnull XmlElement xmlElement,
                                      @Nonnull Transition transition,
                                      @Nonnull Attributable definitionAttributes) {
        
        
    }
    
    /**
     * Parses an {@link XmlElement} for a breakpoint definition.
     * 
     * @param xmlElement the node's {@link XmlElement}
     * @param node the {@link Node}
     * @return the {@link Breakpoint}, if available
     */
    private Breakpoint parseBreakpoint(@Nonnull XmlElement xmlElement,
                                       @Nonnull Node node) {
        
        //
        // do we have an extension?
        //
        XmlElement extensionElement = xmlElement.getElement(BPMN_EXTENSIONS_ELEMENT_NAME);
        if (extensionElement == null) {
            return null;
        }
        
        //
        // create a new breakpoint, if available
        //
        XmlElement breakpointElement = extensionElement.getElementNS(JODAENGINE_EXTENSION_DEBUGGER_NS, "breakpoint");
        
        if (breakpointElement == null) {
            return null;
        }
        
        Breakpoint breakpoint = new BreakpointImpl(node);
        
        //
        // condition available?
        //
        String conditionString = breakpointElement.getAttribute("juelCondition");
        if (conditionString != null) {
            breakpoint.setCondition(new JuelBreakpointCondition(conditionString));
        }
        
        return breakpoint;
    }
    
    /**
     * Any available bpmn control flow construct.
     * 
     * @author Jan Rehwaldt
     * @since 2011-05-24
     */
    private enum BpmnConstructType {
        GATEWAY,
        ACTIVITY,
        EVENT,
        SEQUENCE
    }
}
