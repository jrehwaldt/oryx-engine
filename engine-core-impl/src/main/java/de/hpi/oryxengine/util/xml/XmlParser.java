package de.hpi.oryxengine.util.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * This class represents a XmlParser. It contains
 */
public class XmlParser {

    /** There should only be one {@link SAXParser} in the system. */
    protected static SAXParserFactory defaultSaxParserFactory = SAXParserFactory.newInstance();

    /**
     * Retrieves a {@link XmlParseBuilder} in order to configure the {@link XmlParse XmlParseProcess}.
     *
     * @return the {@link XmlParseBuilder}, in order to configure the {@link XmlParse XmlParseProcess} 
     */
    public XmlParseBuilder getXmlParseBuilder() {

        return new XmlParseBuilderImpl(this);
    }

    protected SAXParser getSaxParser()
    throws Exception {

        return getSaxParserFactory().newSAXParser();
    }

    protected SAXParserFactory getSaxParserFactory() {

        return defaultSaxParserFactory;
    }
}
