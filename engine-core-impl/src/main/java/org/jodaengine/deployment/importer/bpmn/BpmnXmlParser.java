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

package org.jodaengine.deployment.importer.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.util.xml.XmlParseBuilder;
import org.jodaengine.util.xml.XmlParser;


/**
 * Parser for BPMN 2.0 process models.
 * 
 * There is only one instance of this parser in the process engine. This {@link XmlParser} creates {@link BpmnParse}
 * instances that can be used to actually parse the BPMN 2.0 XML process definitions.
 * 
 */
public class BpmnXmlParser extends XmlParser {

    /** The BPMN 2.0 namespace. */
    public static final String BPMN20_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";

    /** The location of the BPMN 2.0 XML schema. */
    public static final String BPMN_20_SCHEMA_LOCATION = "http://www.omg.org/spec/BPMN/20100501/BPMN20.xsd";

    /** The namepace of the BPMN 2.0 diagram interchange elements. */
    public static final String BPMN_DI_NS = "http://www.omg.org/spec/BPMN/20100524/DI";

    /** The namespace of the BPMN 2.0 diagram common elements. */
    public static final String BPMN_DC_NS = "http://www.omg.org/spec/DD/20100524/DC";

    /** The namespace of the generic OMG DI elements (don't ask me why they did not use the BPMN_DI_NS ...). */
    public static final String OMG_DI_NS = "http://www.omg.org/spec/DD/20100524/DI";

    /** The Schema-Instance namespace. */
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    
    /** Namespace for extensions elements of the {@link JodaEngine}. */
    public static final String JODAENGINE_EXTENSIONS_NS = "http://jodaengine.org/bpmn-extensions";
    
    protected List<BpmnXmlParseListener> parseListeners;

    public BpmnXmlParser() {
        
        getParseListeners().add(new BpmnProcessDefinitionValidator());
    }
    
    @Override
    public XmlParseBuilder getXmlParseBuilder() {
    
        return new BpmnXmlParseBuilder(this);
    }
    
    public List<BpmnXmlParseListener> getParseListeners() {

        if (this.parseListeners == null) {
            this.parseListeners = new ArrayList<BpmnXmlParseListener>();
        }
        return this.parseListeners;
    }
}
