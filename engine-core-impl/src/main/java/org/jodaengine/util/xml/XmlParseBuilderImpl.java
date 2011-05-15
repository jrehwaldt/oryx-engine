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

import org.jodaengine.util.io.ClassPathResourceStreamSource;
import org.jodaengine.util.io.InputStreamSource;
import org.jodaengine.util.io.StreamSource;
import org.jodaengine.util.io.StringStreamSource;
import org.jodaengine.util.io.UrlStreamSource;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the basic implementation of the {@link XmlParseBuilder}.
 */
public class XmlParseBuilderImpl implements XmlParseBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected XmlParser xmlParser;
    protected StreamSource streamSource;
    protected String schemaResource;

    /**
     * Default Constrctor.
     * 
     * @param xmlParser
     *            - the {@link XmlParser} that is used by the {@link XmlParse}
     */
    public XmlParseBuilderImpl(XmlParser xmlParser) {

        this.xmlParser = xmlParser;
    }

    @Override
    public XmlParseBuilder defineSourceAsInputStream(InputStream inputStream) {

        defineSourceAsStreamSource(new InputStreamSource(inputStream));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsResource(String resource) {

        return defineSourceAsResource(resource, null);
    }

    @Override
    public XmlParseBuilder defineSourceAsUrl(URL url) {

        defineSourceAsStreamSource(new UrlStreamSource(url));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsResource(String resource, ClassLoader classLoader) {

        defineSourceAsStreamSource(new ClassPathResourceStreamSource(resource, classLoader));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsString(String string) {

        defineSourceAsStreamSource(new StringStreamSource(string));
        return this;
    }

    @Override
    public XmlParseBuilder defineSourceAsStreamSource(StreamSource streamSource) {

        if (this.streamSource != null) {
            String warningMessage = "The StreamSource '" + this.streamSource + "'" + "was overwritten by '"
                + streamSource + "'.";
            logger.warn(warningMessage);
        }
        this.streamSource = streamSource;

        return this;
    }

    @Override
    public XmlParseBuilder defineSchemaResource(String schemaResource) {

        SAXParserFactory saxParserFactory = xmlParser.getSaxParserFactory();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        try {
            saxParserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        this.schemaResource = schemaResource;

        return this;
    }

    /**
     * Creates a new {@link BpmnParse} instance that can be used to parse only one BPMN 2.0 process definition.
     * 
     * @return the specific {@link XmlParse}
     */
    @Override
    public XmlParseable buildXmlParse() {

        if (schemaResource == null || schemaResource.isEmpty()) {
            return new XmlParse(this.xmlParser, streamSource);
        }

        return new XmlParse(this.xmlParser, streamSource, schemaResource);
    }
}
