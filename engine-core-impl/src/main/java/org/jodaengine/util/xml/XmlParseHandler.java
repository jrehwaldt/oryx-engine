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

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is a Handler for the {@link SAXParser}. While parsing through an XML file the {@link SAXParser} calls
 * certain methods. E.g. when the {@link SAXParser} recognizes a new Tag it calls the method
 * {@link #startElement(String, String, String, Attributes) startElement}. In order to handle such parsing events a
 * Handler is needed.
 * 
 * See here for further information: http://tutorials.jenkov.com/java-xml/sax-defaulthandler.html
 * 
 * This Handler is capable of creating an object representation of the XMl files using {@link XmlElement Elements}.
 */
public class XmlParseHandler extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected String defaultNamespace;
    protected XmlParse parse;
    protected Locator locator;
    protected Stack<XmlElement> elementStack = new Stack<XmlElement>();

    public XmlParseHandler(XmlParse parse) {
        super();
        this.parse = parse;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException {

        XmlElement element = new XmlElement(uri, localName, qName, attributes, locator);
        if (elementStack.isEmpty()) {
            parse.rootElement = element;
        } else {
            elementStack.peek().add(element);
        }
        elementStack.push(element);
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {

        elementStack.peek().appendText(String.valueOf(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {

        elementStack.pop();
    }

    public void error(SAXParseException e) {

        parse.getProblemLogger().addError(e);
    }

    public void fatalError(SAXParseException e) {

        parse.getProblemLogger().addError(e);
    }

    public void warning(SAXParseException e) {

        logger.warn(e.toString());
    }

    public void setDocumentLocator(Locator locator) {

        this.locator = locator;
    }

    public void setDefaultNamespace(String defaultNamespace) {

        this.defaultNamespace = defaultNamespace;
    }

}
