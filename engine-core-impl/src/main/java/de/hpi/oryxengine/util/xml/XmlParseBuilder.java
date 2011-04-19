package de.hpi.oryxengine.util.xml;

import java.io.InputStream;
import java.net.URL;

import de.hpi.oryxengine.util.io.StreamSource;

/**
 * This builder helps to create {@link XmlParse} object. Here it is possible to configure the source of the XML file.
 */
public interface XmlParseBuilder {

    /**
     * Defines the source from which the XML can be retrieved.
     * 
     * @param inputStream
     *            - the {@link InputStream} containing the XML
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsInputStream(InputStream inputStream);

    /**
     * Defines the source from which the XML can be retrieved. The resource should be in the classPath.
     * 
     * @param resourcePath
     *            - the classPath of the resource
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsResource(String resourcePath);

    /**
     * Defines the source from which the XML can be retrieved. In this case as {@link URL}.
     * 
     * @param url
     *            - the {@link URL} where the XML file can be found
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsUrl(URL url);

    /**
     * Defines the source from which the XML can be retrieved. The resource should be in the classPath.
     * 
     * @param resourcePath
     *            - the classPath of the resource
     * @param classLoader
     *            - the {@link ClassLoader} which can be used to load the resource.
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsResource(String resourcePath, ClassLoader classLoader);

    /**
     * Defines the source from which the XML can be retrieved. In this case as a plain XML {@link String}.
     * 
     * @param xmlString
     *            - the {@link String} that contains the XML
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsString(String xmlString);

    /**
     * Defines the source from which the XML can be retrieved. In this case it can be any {@link StreamSource}.
     * 
     * @param streamSource
     *            - the {@link StreamSource} containing the XML
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSourceAsStreamSource(StreamSource streamSource);

    /**
     * Sets the schema for the {@link SAXParser}.
     * 
     * @param schemaResource
     *            - this represents the XML schema that should be used by the {@link SAXParser}
     * @return the {@link XmlParseBuilder} in order to continue working
     */
    XmlParseBuilder defineSchemaResource(String schemaResource);

    /**
     * Creates a new {@link BpmnParse} instance that can be used to parse only one BPMN 2.0 process definition.
     * 
     * @return the specific {@link XmlParse}
     */
    XmlParseable buildXmlParse();
}
