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

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.util.xml.XmlParseBuilder;
import org.jodaengine.util.xml.XmlParser;


/**
 * Parser for PetriNet process models.
 * 
 * There is only one instance of this parser in the process engine. This {@link XmlParser} creates {@link PetriNetParse}
 * instances that can be used to actually parse the PetriNet XML process definitions.
 * 
 */
public class PetriNetXmlParser extends XmlParser {

    /** Namespace for extensions elements of the {@link JodaEngine}. */
    public static final String JODAENGINE_EXTENSIONS_NS = "http://jodaengine.org/petri-extensions";
    
    protected List<PetriNetXmlParseListener> parseListeners;

    /**
     * Default constructor.
     */
    public PetriNetXmlParser() {
        
        //getParseListeners().add(new PetriNetProcessDefinitionValidator());
    }
    
    @Override
    public XmlParseBuilder getXmlParseBuilder() {
    
        return new PetriNetXmlParseBuilder(this);
    }
    
    /**
     * Returns a list of specified {@link PetriNetXmlParseListener}s.
     * 
     * @return all registered listeners
     */
    public List<PetriNetXmlParseListener> getParseListeners() {

        if (this.parseListeners == null) {
            this.parseListeners = new ArrayList<PetriNetXmlParseListener>();
        }
        return this.parseListeners;
    }
}
