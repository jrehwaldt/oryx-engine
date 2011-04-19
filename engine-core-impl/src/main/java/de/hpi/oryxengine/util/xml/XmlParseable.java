package de.hpi.oryxengine.util.xml;

import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * This interface represents an object that should implement the logic for parsing through an XML file and translating
 * it into an object.
 */
public interface XmlParseable {

    /**
     * Retrieves the {@link ProcessDefinition} created by this {@link XmlParseable}. 
     * 
     * @return the created {@link ProcessDefinition}
     */
    ProcessDefinition getFinishedProcessDefinition();

    /**
     * This is the central method for executing the XMLParse object. At this point it is possible to parse the XML file
     * or other mappings.
     * 
     * @return this {@link XmlParseable} in order to continue working 
     */
    XmlParseable execute();
}
