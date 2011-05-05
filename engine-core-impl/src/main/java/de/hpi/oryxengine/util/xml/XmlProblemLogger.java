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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * This class helps to log {@link XmlParsingProblem problems} occurring while parsing an XML.
 */
public class XmlProblemLogger {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<XmlParsingProblem> lazyErrors;
    private List<XmlParsingProblem> lazyWarnings;

    private String streamSourceName;

    /**
     * Default instantiation.
     * 
     * @param streamSourceName
     *            - the name of the {@link StreamSource} in order to use it in the logs
     */
    public XmlProblemLogger(String streamSourceName) {

        this.streamSourceName = streamSourceName;
    }

    private List<XmlParsingProblem> getWarnings() {

        if (this.lazyWarnings == null) {
            this.lazyWarnings = new ArrayList<XmlParsingProblem>();
        }
        return this.lazyWarnings;
    }

    /**
     * In case a {@link SAXException} occurred this method will can be called in order to warn that something happens
     * while parsing through a XMl.
     * 
     * @param saxException
     *            - the {@link SAXException} that occurred
     */
    public void addWarning(SAXParseException saxException) {

        getWarnings().add(new XmlParsingProblem(saxException, streamSourceName));
    }

    /**
     * This method can be used to log a warning.
     * 
     * @param warningMessage
     *            - the message that describes the warning
     * @param element
     *            - the {@link XmlElement} where the warning occurres
     */
    public void addWarning(String warningMessage, XmlElement element) {

        getWarnings().add(new XmlParsingProblem(warningMessage, streamSourceName, element));
    }

    /**
     * Says whether there warnings occurred while parsing through the XMl.
     * 
     * @return a {@link Boolean} saying whether there warnings occurred or not.
     */
    public boolean hasWarnings() {

        return !getWarnings().isEmpty();
    }

    /**
     * Logs all warnings that appear while parsing trough the XML.
     */
    public void logWarnings() {

        for (XmlParsingProblem warning : getWarnings()) {
            logger.warn(warning.toString());
        }
    }

    private List<XmlParsingProblem> getErrors() {

        if (this.lazyErrors == null) {
            this.lazyErrors = new ArrayList<XmlParsingProblem>();
        }
        return this.lazyErrors;
    }

    /**
     * In case an error while parsing the XML, this method can be used to log this error.
     * 
     * @param saxParseException
     *            - the {@link SAXParseException} that occurred
     */
    public void addError(SAXParseException saxParseException) {

        getErrors().add(new XmlParsingProblem(saxParseException, streamSourceName));
    }

    /**
     * In case an error while parsing the XML, this method can be used to log this error.
     * 
     * @param errorMessage
     *            - the errorMessage that should be logged
     * @param element
     *            - the {@link XmlElement} where the error occurred
     */
    public void addError(String errorMessage, XmlElement element) {

        getErrors().add(new XmlParsingProblem(errorMessage, streamSourceName, element));
    }

    /**
     * Says whether there errors occurred while parsing through the XMl.
     * 
     * @return a {@link Boolean} saying whether there errors occurred or not.
     */
    public boolean hasErrors() {

        return !getErrors().isEmpty();
    }

    /**
     * Logs all Errors while parsing through the XML. Finally an {@link DalmatinaRuntimeException} is thrown.
     */
    public void throwDalmatinaRuntimeExceptionForErrors() {

        StringBuilder stringBuilder = new StringBuilder();
        for (XmlParsingProblem error : getErrors()) {
            stringBuilder.append(error.toString());
            stringBuilder.append(NEW_LINE);
        }

        logger.error(stringBuilder.toString());
        throw new DalmatinaRuntimeException(stringBuilder.toString());
    }
}
