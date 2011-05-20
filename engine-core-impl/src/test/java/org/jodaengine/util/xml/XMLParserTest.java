package org.jodaengine.util.xml;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.io.IoUtil;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Tests the XMLParser with a simple xml file.
 */
public class XMLParserTest {

    private final static String XML_TEST_RESOURCE = "org/jodaengine/util/xml/test-xml-for-parser.xml";

    private String failureMessage;

    public String getFaliureMessage() {

        if (this.failureMessage != null) {
            return this.failureMessage;
        }

        String resultString;
        URL fileURL = ReflectionUtil.getResource(XML_TEST_RESOURCE);
        try {
            resultString = "\n" + IoUtil.readFileAsString(new File(fileURL.toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            resultString = "The the XML test resource can be found here: " + XML_TEST_RESOURCE;
        }
        this.failureMessage = resultString;
        return resultString;
    }

    @Test
    public void testProcessingXML() {

        XmlParser parser = new XmlParser();

        XmlParseBuilder parseBuilder = parser.getXmlParseBuilder();
        parseBuilder.defineSourceAsResource(XML_TEST_RESOURCE);
        XmlParse xmlParse = (XmlParse) parseBuilder.buildXmlParse();
        xmlParse.execute();

        Assert.assertEquals(xmlParse.getRootElement().tagName, "root", getFaliureMessage());
        List<XmlElement> elements = xmlParse.getRootElement().getElements();
        Assert.assertTrue(elements.size() == 2, getFaliureMessage());

        XmlElement firstChild = elements.get(0);
        Assert.assertEquals(firstChild.getAttribute("id"), "first_child", getFaliureMessage());
        Assert.assertEquals(elements.get(0).getElements().get(0).getText(), "text 1", getFaliureMessage());

        XmlElement secondChild = elements.get(1);
        Assert.assertEquals(secondChild.getAttributeNS("ls-ns", "as"), "123", getFaliureMessage());
    }

    @Test
    public void testProcessingXM1L() {
        
        XmlParser parser = new XmlParser();
        
        XmlParseBuilder parseBuilder = parser.getXmlParseBuilder();
        parseBuilder.defineSourceAsResource(XML_TEST_RESOURCE);
        XmlParse xmlParse = (XmlParse) parseBuilder.buildXmlParse();
        xmlParse.execute();
        
        String str =xmlParse.getRootElement().getElements().get(0).getAttributeNS("http://www.signavio.com", "form");
        System.out.println(str);
    }
}
