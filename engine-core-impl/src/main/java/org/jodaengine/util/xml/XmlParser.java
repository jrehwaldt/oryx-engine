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

        defaultSaxParserFactory.setNamespaceAware(true);
        return defaultSaxParserFactory;
    }
}
