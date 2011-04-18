package de.hpi.oryxengine.util.xml;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParse;
import de.hpi.oryxengine.util.io.InputStreamSource;
import de.hpi.oryxengine.util.io.ResourceStreamSource;
import de.hpi.oryxengine.util.io.StreamSource;
import de.hpi.oryxengine.util.io.StringStreamSource;
import de.hpi.oryxengine.util.io.UrlStreamSource;

/**
 * The {@link XmlParse} triggers the saxParser in order to parse through the xml. This class is designed to be inherited
 * from in order to implement custom parse behaviour and convert the XML into objects, e.g. see {@link BpmnXmlParse}.
 */
public class XmlParse {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String NEW_LINE = System.getProperty("line.separator");

    protected XmlParser parser;
    protected String name;
    protected StreamSource streamSource;
    protected XmlElement rootElement = null;
    protected List<XmlParsingProblem> errors = new ArrayList<XmlParsingProblem>();
    protected List<XmlParsingProblem> warnings = new ArrayList<XmlParsingProblem>();
    protected String schemaResource;

    public XmlParse(XmlParser parser) {

        this.parser = parser;
    }

    public XmlParse name(String name) {

        this.name = name;
        return this;
    }

    public XmlParse sourceInputStream(InputStream inputStream) {

        if (name == null) {
            name("inputStream");
        }
        setStreamSource(new InputStreamSource(inputStream));
        return this;
    }

    public XmlParse sourceResource(String resource) {

        return sourceResource(resource, null);
    }

    public XmlParse sourceUrl(URL url) {

        if (name == null) {
            name(url.toString());
        }
        setStreamSource(new UrlStreamSource(url));
        return this;
    }

    public XmlParse sourceUrl(String url) {

        try {
            return sourceUrl(new URL(url));
        } catch (MalformedURLException e) {
            String errorMessage = "The URL '" + url + "' is malformed.";
            throw new DalmatinaRuntimeException(errorMessage, e);
        }
    }

    public XmlParse sourceResource(String resource, ClassLoader classLoader) {

        if (name == null) {
            name(resource);
        }
        setStreamSource(new ResourceStreamSource(resource, classLoader));
        return this;
    }

    public XmlParse sourceString(String string) {

        if (name == null) {
            name("string");
        }
        setStreamSource(new StringStreamSource(string));
        return this;
    }

    protected void setStreamSource(StreamSource streamSource) {

        if (this.streamSource != null) {
            String errorMessage = "Invalid StreamSource: multiple sources " + this.streamSource + " and "
                + streamSource;
            throw new DalmatinaRuntimeException(errorMessage);
        }
        this.streamSource = streamSource;
    }

    public XmlParse execute() {

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
            String errorMessage = "The Stream '" + name + "' could not be parsed. Following error ocurred: "
                + e.getMessage();
            throw new DalmatinaRuntimeException(errorMessage, e);
        }

        return this;
    }

    public XmlElement getRootElement() {

        return rootElement;
    }

    public List<XmlParsingProblem> getProblems() {

        return errors;
    }

    public void addError(SAXParseException e) {

        errors.add(new XmlParsingProblem(e, name));
    }

    public void addError(String errorMessage, XmlElement element) {

        errors.add(new XmlParsingProblem(errorMessage, name, element));
    }

    public boolean hasErrors() {

        return errors != null && !errors.isEmpty();
    }

    public void addWarning(SAXParseException e) {

        warnings.add(new XmlParsingProblem(e, name));
    }

    public void addWarning(String errorMessage, XmlElement element) {

        warnings.add(new XmlParsingProblem(errorMessage, name, element));
    }

    public boolean hasWarnings() {

        return warnings != null && !warnings.isEmpty();
    }

    public void logWarnings() {

        for (XmlParsingProblem warning : warnings) {
            logger.warn(warning.toString());
        }
    }

    public void throwDalmatinaRuntimeExceptionForErrors() {

        StringBuilder strb = new StringBuilder();
        for (XmlParsingProblem error : errors) {
            strb.append(error.toString());
            strb.append(NEW_LINE);
        }
        throw new DalmatinaRuntimeException(strb.toString());
    }

    public void setSchemaResource(String schemaResource) {

        SAXParserFactory saxParserFactory = parser.getSaxParserFactory();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        try {
            saxParserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        this.schemaResource = schemaResource;
    }
}
