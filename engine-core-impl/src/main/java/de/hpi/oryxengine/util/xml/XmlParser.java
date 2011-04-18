package de.hpi.oryxengine.util.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * 
 * @author Tom Baeyens
 */
public class XmlParser {

  protected static SAXParserFactory defaultSaxParserFactory = SAXParserFactory.newInstance();
  
  public static final XmlParser INSTANCE = new XmlParser();

  public XmlParse createParse() {
    return new XmlParse(this);
  }

  protected SAXParser getSaxParser() throws Exception {
    return getSaxParserFactory().newSAXParser();
  }

  protected SAXParserFactory getSaxParserFactory() {
    return defaultSaxParserFactory;
  }
}
