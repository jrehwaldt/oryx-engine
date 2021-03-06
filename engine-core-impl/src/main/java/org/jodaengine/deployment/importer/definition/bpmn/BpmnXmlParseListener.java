/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This code is part of the Activiti project under the above license:
 * 
 *                  http://www.activiti.org
 * 
 * We did some modification which are hereby also under the Apache License, Version 2.0.
 */

package org.jodaengine.deployment.importer.definition.bpmn;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.xml.XmlElement;


/**
 * If it is necessary to attach while parsing through a BPMN serialized XML file, you can use this class.
 */
public interface BpmnXmlParseListener {
    
    String BPMN_EXTENSIONS_ELEMENT_NAME = "extensionElements";
    
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
    void parseProcess(XmlElement processXmlElement,
                      ProcessDefinition processDefinition);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node startNode}. Here you can attach some
     * afterwork. For example persistence.
     * 
     * @param startEventXmlElement
     *            - the original {@link XmlElement startEventXMLElement}
     * @param startNode
     *            - the created {@link Node startNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseStartEvent(XmlElement startEventXmlElement,
                         Node startNode,
                         Attributable definitionScopeAttributable);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node exclusiveGatewayNode}.
     * 
     * @param exclusiveGatewayXmlElement
     *            - the original {@link XmlElement exclusiveGatewayXmlElement}
     * @param exclusiveGatewayNode
     *            - the created {@link Node exclusiveGatewayNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement,
                               Node exclusiveGatewayNode,
                               Attributable definitionScopeAttributable);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node parallelGatewayNode}.
     * 
     * @param parallelGatewayXmlElement
     *            - the original {@link XmlElement parallelGatewayXmlElement}
     * @param parallelGatewayNode
     *            - the created {@link Node parallelGatewayNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseParallelGateway(XmlElement parallelGatewayXmlElement,
                              Node parallelGatewayNode,
                              Attributable definitionScopeAttributable);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node taskNode}.
     * 
     * @param taskXmlElement
     *            - the original {@link XmlElement taskXmlElement}
     * @param taskNode
     *            - the created {@link Node taskNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseTask(XmlElement taskXmlElement,
                   Node taskNode,
                   Attributable definitionScopeAttributable);

    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node userTaskNode}.
     * 
     * @param userTaskXmlElement
     *            - the original {@link XmlElement userTaskXmlElement}
     * @param userTaskNode
     *            - the created {@link Node userTaskNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseUserTask(XmlElement userTaskXmlElement,
                       Node userTaskNode,
                       Attributable definitionScopeAttributable);
    
    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link Node endEventNode}.
     * 
     * @param endEventXmlElemnt
     *            - the original {@link XmlElement endEventXmlElemnt}
     * @param endEventNode
     *            - the created {@link Node endEventNode}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseEndEvent(XmlElement endEventXmlElemnt,
                       Node endEventNode,
                       Attributable definitionScopeAttributable);
    
    
    /**
     * Is called when the {@link BpmnXmlParser} finished creating the {@link ControlFlow}.
     * 
     * @param sequenceFlowElement
     *            - the original {@link XmlElement sequenceFlowElement}
     * @param controlFlow
     *            - the created {@link ControlFlow}
     * @param definitionScopeAttributable
     *            - a globally (within this definition) available {@link Attributable}
     */
    void parseSequenceFlow(XmlElement sequenceFlowElement,
                           ControlFlow controlFlow,
                           Attributable definitionScopeAttributable);
}
