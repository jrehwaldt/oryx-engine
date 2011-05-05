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

package de.hpi.oryxengine.util.xml;

import org.xml.sax.SAXParseException;

/**
 * Parse error class.
 */
public class XmlParsingProblem {

    protected String errorMessage;
    protected String resource;
    protected int line;
    protected int column;

    public XmlParsingProblem(SAXParseException e, String resource) {

        Throwable exception = e;
        while (exception != null) {
            if (this.errorMessage == null) {
                this.errorMessage = exception.getMessage();
            } else {
                this.errorMessage += ": " + exception.getMessage();
            }
            exception = exception.getCause();
        }
        this.resource = resource;
        this.line = e.getLineNumber();
        this.column = e.getColumnNumber();
    }

    public XmlParsingProblem(String errorMessage, String resourceName, XmlElement element) {

        this.errorMessage = errorMessage;
        this.resource = resourceName;
        if (element != null) {
            this.line = element.getLine();
            this.column = element.getColumn();
        }
    }

    public String toString() {

        return errorMessage + (resource != null ? " | " + resource : "") + " | line " + line + " | column " + column;
    }
}
