package de.hpi.oryxengine.util.xml;

import java.io.BufferedReader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.util.io.ResourceStreamSource;

/**
 * Tests the XMLParser with a simple xml file.
 */
public class XMLParserTest {

    private final static String XML_TEST_RESOURCE = "de/hpi/oryxengine/util/xml/test-xml-for-parser.xml";   
  
    private static String FAILURE_MESSAGE = getFaliureMessage();

    public static String getFaliureMessage() {
        
        StringBuilder stringBuilder = new StringBuilder("The XML test file looks like this: \n");
        stringBuilder.
        
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new );
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
        stringBuilder.append(sb)
        return stringBuilder.toString();
    }
    
    @Test
    public void testProcessingXML() {

        Parser parser = new Parser();

        Parse parse = parser.createParse();
        parse.setStreamSource(new ResourceStreamSource(XML_TEST_RESOURCE));
        parse.execute();

        Assert.assertEquals(parse.rootElement.tagName, "root", FAILURE_MESSAGE);
        List<Element> elements = parse.rootElement.getElements();
        Assert.assertTrue(elements.size() == 2, text, FAILURE_MESSAGE);
        Assert.assertEquals(elements.get(0).getElements().get(0).getText(), "text 1", FAILURE_MESSAGE);
    }
}
