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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;


/**
 * Represents one XML element. 
 */
public class XmlElement {

    protected String uri;
    protected String tagName;

    /**
     * This {@link Map} stores the attributes of the XML.
     * 
     * The Key of the {@link Map} consists of the following: nsUri + : + attributeName
     * E.g. xsl:id , see http://www.w3.org/TR/xml-names/#uniqAttrs
     * 
     * If namespace is empty, key is only the attributeName, e.g. id .
     */
    protected Map<String, XmlAttribute> attributeMap = new HashMap<String, XmlAttribute>();

    protected int line;
    protected int column;
    protected StringBuilder text = new StringBuilder();
    protected List<XmlElement> elements = new ArrayList<XmlElement>();

    public XmlElement(String uri, String localName, String qName, Attributes attributes, Locator locator) {

        this.uri = uri;
        if (uri == null || uri.isEmpty()) {
            this.tagName = qName;
        } else {
            this.tagName = localName;
        }

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {

                String attributeUri = attributes.getURI(i);
                String value = attributes.getValue(i);
                String name;
                if (attributeUri == null || attributeUri.isEmpty()) {
                    name = attributes.getQName(i);
                } else {
                    name = attributes.getLocalName(i);
                }
                this.attributeMap.put(composeMapKey(attributeUri, name), new XmlAttribute(name, value, attributeUri));
            }
        }

        if (locator != null) {
            line = locator.getLineNumber();
            column = locator.getColumnNumber();
        }
    }

    public List<XmlElement> getElements(String tagName) {

        return getElementsNS(null, tagName);
    }

    public List<XmlElement> getElementsNS(String nameSpaceUri, String tagName) {

        List<XmlElement> selectedElements = new ArrayList<XmlElement>();
        for (XmlElement element : elements) {
            if (tagName.equals(element.getTagName())) {
                if (nameSpaceUri == null || (nameSpaceUri != null && nameSpaceUri.equals(element.getUri()))) {
                    selectedElements.add(element);
                }
            }
        }
        return selectedElements;
    }

    public XmlElement getElement(String tagName) {

        return getElementNS(null, tagName);
    }

    public XmlElement getElementNS(String nameSpaceUri, String tagName) {

        List<XmlElement> elements = getElementsNS(nameSpaceUri, tagName);
        if (elements.size() == 0) {
            return null;
        } else if (elements.size() > 1) {
            String errorMessage = "Parsing exception: multiple elements with tag name " + tagName + " found.";
            throw new JodaEngineRuntimeException(errorMessage);
        }
        return elements.get(0);
    }

    public void add(XmlElement element) {

        elements.add(element);
    }

    public String getAttribute(String name) {

        if (attributeMap.containsKey(name)) {
            return attributeMap.get(name).getValue();
        }
        return null;
    }

    public Set<String> getAttributes() {

        return attributeMap.keySet();
    }

    public String getAttributeNS(String namespaceUri, String name) {

        return getAttribute(composeMapKey(namespaceUri, name));
    }

    public String getAttribute(String name, String defaultValue) {

        if (attributeMap.containsKey(name)) {
            return attributeMap.get(name).getValue();
        }
        return defaultValue;
    }

    public String getAttributeNS(String namespaceUri, String name, String defaultValue) {

        return getAttribute(composeMapKey(namespaceUri, name), defaultValue);
    }

    protected String composeMapKey(String attributeUri, String attributeName) {

        StringBuilder strb = new StringBuilder();
        if (attributeUri != null && !attributeUri.isEmpty()) {
            strb.append(attributeUri);
            strb.append(":");
        }
        strb.append(attributeName);
        return strb.toString();
    }

    public List<XmlElement> getElements() {

        return elements;
    }

    public String toString() {

        return "<" + tagName + "...";
    }

    public String getUri() {

        return uri;
    }

    public String getTagName() {

        return tagName;
    }

    public int getLine() {

        return line;
    }

    public int getColumn() {

        return column;
    }

    /**
     * Appends a certain {@link String} to the text of this {@link XmlElement}.
     * 
     * Due to the nature of SAX parsing, sometimes the characters of an element are not processed at once. So instead of
     * a setText operation, we need to have an appendText operation.
     *
     * @param textToBeAppended - the {@link String} that will be appended.
     */
    public void appendText(String textToBeAppended) {

        this.text.append(textToBeAppended);
    }

    public String getText() {

        return text.toString();
    }
}
