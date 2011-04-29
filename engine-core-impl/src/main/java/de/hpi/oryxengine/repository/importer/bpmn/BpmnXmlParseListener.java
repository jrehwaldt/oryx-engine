package de.hpi.oryxengine.repository.importer.bpmn;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.util.xml.XmlElement;

/**
 * If it is necessary to attach while parsing through a BPMN serialized XML file, you can use this class.
 */
public interface BpmnXmlParseListener {

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link ProcessDefinition}. Here you can attach
     * some afterwork. For example reporting.
     * 
     * @param processXmlElement
     *            - the original {@link XmlElement processXMLElement}
     * 
     * @param processDefinition
     *            - the created {@link ProcessDefinition}
     */
    void parseProcess(XmlElement processXmlElement, ProcessDefinition processDefinition);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node startNode}. Here you can attach some
     * afterwork. For example persistence.
     * 
     * @param startEventXmlElement
     *            - the original {@link XmlElement startEventXMLElement}
     * @param startNode
     *            - the created {@link Node startNode}
     */
    void parseStartEvent(XmlElement startEventXmlElement, Node startNode);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node exclusiveGatewayNode}.
     * 
     * @param exclusiveGatewayXmlElement
     *            - the original {@link XmlElement exclusiveGatewayXmlElement}
     * @param exclusiveGatewayNode
     *            - the created {@link Node exclusiveGatewayNode}
     */
    void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement, Node exclusiveGatewayNode);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node parallelGatewayNode}.
     * 
     * @param parallelGatewayXmlElement
     *            - the original {@link XmlElement parallelGatewayXmlElement}
     * @param parallelGatewayNode
     *            - the created {@link Node parallelGatewayNode}
     */
    void parseParallelGateway(XmlElement parallelGatewayXmlElement, Node parallelGatewayNode);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node taskNode}.
     * 
     * @param taskXmlElement
     *            - the original {@link XmlElement parallelGatewayXmlElement}
     * @param taskNode
     *            - the created {@link Node taskNode}
     */
    void parseTask(XmlElement taskXmlElement, Node taskNode);
    
    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node endEventNode}.
     * 
     * @param endEventXmlElemnt
     *            - the original {@link XmlElement endEventXmlElemnt}
     * @param endEventNode
     *            - the created {@link Node endEventNode}
     */
    void parseEndEvent(XmlElement endEventXmlElemnt, Node endEventNode);
    
    
    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Transition transition}.
     * 
     * @param sequenceFlowElement
     *            - the original {@link XmlElement sequenceFlowElement}
     * @param transition
     *            - the created {@link Transition transition}
     */
    void parseSequenceFlow(XmlElement sequenceFlowElement, Transition transition);
}
