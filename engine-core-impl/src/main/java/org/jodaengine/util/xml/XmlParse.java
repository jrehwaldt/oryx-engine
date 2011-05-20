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

package org.jodaengine.util.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParse;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.io.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@link XmlParse} triggers the saxParser in order to parse through the xml. This class is designed to be inherited
 * from in order to implement custom parse behaviour and convert the XML into objects, e.g. see {@link BpmnXmlParse}.
 */
public class XmlParse implements XmlParseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    protected XmlParser parser;

    protected StreamSource streamSource;

    protected XmlElement rootElement;

    private XmlProblemLogger problemLogger;

    protected String schemaResource;

    /**
     * Instantiates a new xml parser without a schemaSource. See javaDoc comment of the more complete constructor.
     *
     * @param parser the parser
     * @param streamSource the stream source
     */
    public XmlParse(XmlParser parser, StreamSource streamSource) {

        this(parser, streamSource, null);
    }

    /**
     * Default Constructor.
     * 
     * @param xmlParser
     *            - the {@link XmlParser} containing information about the concrete parse process.
     * @param streamSource
     *            - the {@link StreamSource} that contains the XML that should be parsed
     * @param schemaResource
     *            - this represents the XML schema that should be used by the {@link SAXParser}
     */
    public XmlParse(XmlParser xmlParser, StreamSource streamSource, String schemaResource) {

        this.parser = xmlParser;
        this.streamSource = streamSource;
        this.schemaResource = schemaResource;

        // Prepare fields for execution
        this.rootElement = null;
        this.problemLogger = new XmlProblemLogger(streamSource.getName());
    }

    @Override
    public ProcessDefinition getFinishedProcessDefinition() {

        String errorMessage = "This class does not support the generation of a process definintion."
            + "This class is supposed to be inherited from.";
        throw new UnsupportedOperationException(errorMessage);
    }

    @Override
    public XmlParseable execute() {

        try {
            InputStream inputStream = streamSource.getInputStream();

            if (schemaResource == null) {
                // must be done before parser is created
                parser.getSaxParserFactory().setNamespaceAware(false);
                parser.getSaxParserFactory().setValidating(false);
            }

            SAXParser saxParser = parser.getSaxParser();
            if (schemaResource != null) {
                saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                saxParser.setProperty(JAXP_SCHEMA_SOURCE, schemaResource);
            }
            saxParser.parse(inputStream, new XmlParseHandler(this));

        } catch (Exception e) {
            String errorMessage = "The Stream '" + streamSource.getName()
                + "' could not be parsed. Following error ocurred: " + e.getMessage();
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }

        return this;
    }

    /**
     * This class only parses through the XML and creates an object model of the XML. That's why this method retrieves
     * the {@link XmlElement rootElement} of the XML.
     * 
     * @return the {@link XmlElement rootElement}
     */
    public XmlElement getRootElement() {

        return rootElement;
    }

    /**
     * Gets the problem logger.
     *
     * @return the XML problem logger
     */
    public XmlProblemLogger getProblemLogger() {

        return problemLogger;
    }
}
