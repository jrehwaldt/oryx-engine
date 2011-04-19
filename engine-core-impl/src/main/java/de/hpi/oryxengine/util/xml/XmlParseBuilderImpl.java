package de.hpi.oryxengine.util.xml;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.util.io.InputStreamSource;
import de.hpi.oryxengine.util.io.ClassPathResourceStreamSource;
import de.hpi.oryxengine.util.io.StreamSource;
import de.hpi.oryxengine.util.io.StringStreamSource;
import de.hpi.oryxengine.util.io.UrlStreamSource;

/**
 * This is the basic implementation of the {@link XmlParseBuilder}.
 */
public class XmlParseBuilderImpl implements XmlParseBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected XmlParser xmlParser;
    protected StreamSource streamSource;
    protected String schemaResource;

    /**
     * Default Constrctor.
     * 
     * @param xmlParser
     *            - the {@link XmlParser} that is used by the {@link XmlParse}
     */
    public XmlParseBuilderImpl(XmlParser xmlParser) {

        this.xmlParser = xmlParser;
    }

    @Override
    public XmlParseBuilder defineSourceAsInputStream(InputStream inputStream) {

        defineSourceAsStreamSource(new InputStreamSource(inputStream));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsResource(String resource) {

        return defineSourceAsResource(resource, null);
    }

    @Override
    public XmlParseBuilder defineSourceAsUrl(URL url) {

        defineSourceAsStreamSource(new UrlStreamSource(url));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsResource(String resource, ClassLoader classLoader) {

        defineSourceAsStreamSource(new ClassPathResourceStreamSource(resource, classLoader));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsString(String string) {

        defineSourceAsStreamSource(new StringStreamSource(string));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsStreamSource(StreamSource streamSource) {

        if (this.streamSource != null) {
            String warningMessage = "The StreamSource '" + this.streamSource + "'" + "was overwritten by '"
                + streamSource + "'.";
            logger.warn(warningMessage);
        }
        this.streamSource = streamSource;

        return this;
    }

    @Override
    public XmlParseBuilder defineSchemaResource(String schemaResource) {

        SAXParserFactory saxParserFactory = xmlParser.getSaxParserFactory();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        try {
            saxParserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        this.schemaResource = schemaResource;

        return this;
    }

    /**
     * Creates a new {@link BpmnParse} instance that can be used to parse only one BPMN 2.0 process definition.
     * 
     * @return the specific {@link XmlParse}
     */
    @Override
    public XmlParseable buildXmlParse() {

        if (schemaResource == null || schemaResource.isEmpty()) {
            return new XmlParse(this.xmlParser, streamSource);
        }

        return new XmlParse(this.xmlParser, streamSource, schemaResource);
    }
}
