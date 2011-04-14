package de.hpi.oryxengine.util.xml;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.util.IoUtil;
import de.hpi.oryxengine.util.ReflectionUtil;
import de.hpi.oryxengine.util.io.ResourceStreamSource;

/**
 * Tests the XMLParser with a simple xml file.
 */
public class XMLParserTest {

    private final static String XML_TEST_RESOURCE = "de/hpi/oryxengine/util/xml/test-xml-for-parser.xml";   
 
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
            resultString ="The the XML test resource can be found here: " + XML_TEST_RESOURCE;
        }
        this.failureMessage = resultString;
        return resultString;
    }
    
    @Test
    public void testProcessingXML() {

        XmlParser parser = new XmlParser();

        XmlParse parse = parser.createParse();
        parse.setStreamSource(new ResourceStreamSource(XML_TEST_RESOURCE));
        parse.execute();

        Assert.assertEquals(parse.rootElement.tagName, "root", getFaliureMessage());
        List<Element> elements = parse.rootElement.getElements();
        Assert.assertTrue(elements.size() == 2, getFaliureMessage());
        
        Element firstChild = elements.get(0);
        Assert.assertEquals(firstChild.getAttribute("id"), "first_child", getFaliureMessage());
        Assert.assertEquals(elements.get(0).getElements().get(0).getText(), "text 1", getFaliureMessage());

        Element secondChild = elements.get(1);
        Assert.assertEquals(secondChild.getAttributeNS("ls", "as"), "123", getFaliureMessage());
    }
}
