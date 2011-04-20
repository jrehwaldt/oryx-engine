package de.hpi.oryxengine.util.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParse;
import de.hpi.oryxengine.util.io.StreamSource;

/**
 * The {@link XmlParse} triggers the saxParser in order to parse through the xml. This class is designed to be inherited
 * from in order to implement custom parse behaviour and convert the XML into objects, e.g. see {@link BpmnXmlParse}.
 */
public class XmlParse implements XmlParseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String NEW_LINE = System.getProperty("line.separator");

    protected XmlParser parser;
    protected StreamSource streamSource;
    protected XmlElement rootElement = null;
    protected List<XmlParsingProblem> lazyErrors;
    protected List<XmlParsingProblem> lazyWarnings;
    protected String schemaResource;

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
            String errorMessage = "The Stream '" + streamSource.getName() + "' could not be parsed. Following error ocurred: "
                + e.getMessage();
            throw new DalmatinaRuntimeException(errorMessage, e);
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

    protected List<XmlParsingProblem> getWarnings() {

        if (lazyWarnings == null) {
            lazyWarnings = new ArrayList<XmlParsingProblem>();
        }
        return lazyWarnings;
    }

    /**
     * In case a {@link SAXException} occurred this method will can be called in order to warn that something happens
     * while parsing through a XMl.
     * 
     * @param saxE
     *            - the {@link SAXException} that occurred
     */
    protected void addWarning(SAXParseException saxE) {

        getWarnings().add(new XmlParsingProblem(saxE, streamSource.getName()));
    }

    /**
     * This method can be used to log a warning.
     * 
     * @param warningMessage
     *            - the message that describes the warning
     * @param element
     *            - the {@link XmlElement} where the warning occurres
     */
    protected void addWarning(String warningMessage, XmlElement element) {

        getWarnings().add(new XmlParsingProblem(warningMessage, streamSource.getName(), element));
    }

    /**
     * Says whether there warnings occurred while parsing through the XMl.
     * 
     * @return a {@link Boolean} saying whether there warnings occurred or not.
     */
    public boolean hasWarnings() {

        return !getWarnings().isEmpty();
    }

    /**
     * Logs all warnings that appear while parsing trough the XML.
     */
    public void logWarnings() {

        for (XmlParsingProblem warning : getWarnings()) {
            logger.warn(warning.toString());
        }
    }

    protected List<XmlParsingProblem> getErrors() {

        if (lazyErrors == null) {
            lazyErrors = new ArrayList<XmlParsingProblem>();
        }
        return lazyErrors;
    }

    protected void addError(SAXParseException e) {

        getErrors().add(new XmlParsingProblem(e, streamSource.getName()));
    }

    protected void addError(String errorMessage, XmlElement element) {

        getErrors().add(new XmlParsingProblem(errorMessage, streamSource.getName(), element));
    }

    /**
     * Says whether there errors occurred while parsing through the XMl.
     * 
     * @return a {@link Boolean} saying whether there errors occurred or not.
     */
    public boolean hasErrors() {

        return !getErrors().isEmpty();
    }

    /**
     * Logs all Errors while parsing through the XML. Finally an {@link DalmatinaRuntimeException} is thrown.
     */
    public void throwDalmatinaRuntimeExceptionForErrors() {

        StringBuilder stringBuilder = new StringBuilder();
        for (XmlParsingProblem error : getErrors()) {
            stringBuilder.append(error.toString());
            stringBuilder.append(NEW_LINE);
        }

        logger.error(stringBuilder.toString());
        throw new DalmatinaRuntimeException(stringBuilder.toString());
    }
}
