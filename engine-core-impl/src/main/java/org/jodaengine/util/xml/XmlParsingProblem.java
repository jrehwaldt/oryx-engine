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

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parse error class.
 */
public class XmlParsingProblem {

    protected String errorMessage;
    protected String xmlResourceName;
    protected int line;
    protected int column;

    /**
     * Default constructor for instantiating this class.
     * 
     * @param saxException
     *            - a {@link SAXException} that occurs while parsing the XML
     * @param xmlResourceName
     *            - the name of the XMl resource where the problems occurred
     */
    public XmlParsingProblem(SAXParseException saxException, String xmlResourceName) {

        Throwable exception = saxException;
        while (exception != null) {
            if (this.errorMessage == null) {
                this.errorMessage = exception.getMessage();
            } else {
                this.errorMessage += ": " + exception.getMessage();
            }
            exception = exception.getCause();
        }
        this.xmlResourceName = xmlResourceName;
        this.line = saxException.getLineNumber();
        this.column = saxException.getColumnNumber();
    }

    /**
     * Another default constructor for an {@link XmlParsingProblem} in case there occurred a problem while processing
     * the XML resource.
     * 
     * @param errorMessage
     *            - the error message describing the problem
     * @param xmlResourceName
     *            - the name of the XMl resource where the problems occurred
     * @param xmlElement
     *            - the {@link XmlElement xml tag} where the problem occurred
     */
    public XmlParsingProblem(String errorMessage, String xmlResourceName, XmlElement xmlElement) {

        this.errorMessage = errorMessage;
        this.xmlResourceName = xmlResourceName;
        if (xmlElement != null) {
            this.line = xmlElement.getLine();
            this.column = xmlElement.getColumn();
        }
    }

    /**
     * Transforms the {@link XmlParsingProblem} into a {@link String}.
     * 
     * @return a String representing a {@link XmlParsingProblem}
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(this.errorMessage);
        
        if (xmlResourceName != null) {
            stringBuilder.append(" | " + this.xmlResourceName);
        }

        stringBuilder.append(" | line " + this.line);
        stringBuilder.append(" | column " + this.column);
        
        return stringBuilder.toString(); 
    }
}
