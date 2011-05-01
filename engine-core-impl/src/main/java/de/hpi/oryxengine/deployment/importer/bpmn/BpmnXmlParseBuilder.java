package de.hpi.oryxengine.deployment.importer.bpmn;

import de.hpi.oryxengine.util.xml.XmlParseBuilderImpl;

/**
 * This builder helps to create {@link BpmnXmlParse} object. Here it is possible to configure the source of the XML
 * file.
 */
public class BpmnXmlParseBuilder extends XmlParseBuilderImpl {

    protected BpmnXmlParser bpmnXmlParser;

    public BpmnXmlParseBuilder(BpmnXmlParser bpmnXmlParser) {

        super(bpmnXmlParser);
        this.bpmnXmlParser = bpmnXmlParser;
    }

    @Override
    public BpmnXmlParse buildXmlParse() {

        return new BpmnXmlParse(bpmnXmlParser, streamSource);
    }
}
