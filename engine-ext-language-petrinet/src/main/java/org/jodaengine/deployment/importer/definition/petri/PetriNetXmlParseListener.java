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

package org.jodaengine.deployment.importer.definition.petri;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.xml.XmlElement;


/**
 * If it is necessary to attach while parsing through a PetriNet serialized XML file, you can use this class.
 */
public interface PetriNetXmlParseListener {
    
    String PETRI_EXTENSIONS_ELEMENT_NAME = "extensionElements";
    
    /**
     * Is called when the {@link PetriNetXmlParser} finished creating the {@link ProcessDefinition}. Here you can attach
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
     * Is called when the {@link PetriNetXmlParser} finished creating the {@link Node place}.
     *
     * @param placeXmlElement the place xml element
     * @param place the created {@link Node}
     * @param definitionScopeAttributable the definition scope attributable
     */
    void parsePlace(XmlElement placeXmlElement,
                               Node place,
                               Attributable definitionScopeAttributable);

    /**
     * Is called when the {@link PetriNetXmlParser} finished creating the {@link Node transition}.
     *
     * @param transitionXmlElement the transition xml element
     * @param transition the created {@link Node}
     * @param definitionScopeAttributable the definition scope attributable
     */
    void parseTransition(XmlElement transitionXmlElement,
                               Node transition,
                               Attributable definitionScopeAttributable);

    
    /**
     * Is called when the {@link PetriNetXmlParser} finished creating the {@link ControlFlow}.
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
