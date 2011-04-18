package de.hpi.oryxengine.repository.importer.bpmn;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.util.xml.XmlParser;

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
    public static final String BPMN_20_SCHEMA_LOCATION = "org/activiti/impl/bpmn/parser/BPMN20.xsd";

    /** The namepace of the BPMN 2.0 diagram interchange elements. */
    public static final String BPMN_DI_NS = "http://www.omg.org/spec/BPMN/20100524/DI";

    /** The namespace of the BPMN 2.0 diagram common elements. */
    public static final String BPMN_DC_NS = "http://www.omg.org/spec/DD/20100524/DC";

    /** The namespace of the generic OMG DI elements (don't ask me why they did not use the BPMN_DI_NS ...). */
    public static final String OMG_DI_NS = "http://www.omg.org/spec/DD/20100524/DI";

    /** The Schema-Instance namespace. */
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    
    /** Namesspace for extensions elements of the {@link OryxEngine}. */
    public static final String JODA_ENGINE_EXTENSIONS_NS = "http://joda-engine.org/bpmn";

    protected List<BpmnXmlParseListener> parseListeners;

    /**
     * Creates a new {@link BpmnParse} instance that can be used to parse only one BPMN 2.0 process definition.
     */
    public BpmnXmlParse createParse() {

        return new BpmnXmlParse(this);
    }

    public List<BpmnXmlParseListener> getParseListeners() {

        if (this.parseListeners == null) {
            this.parseListeners = new ArrayList<BpmnXmlParseListener>();
        }
        return this.parseListeners;
    }
}
