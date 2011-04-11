package de.hpi.oryxengine.rest.provider;

import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * UUID un/marshaller adapter.
 * 
 * http://dev.e-taxonomy.eu/trac/browser/trunk/cdmlib/cdmlib-model/src/main/java/eu/etaxonomy/cdm/jaxb/UUIDAdapter.java
 */
public class UUIDXmlAdapter extends XmlAdapter<String, UUID> {

    public static final String UUID_URN_PREFIX = "urn-uuid-";

    @Override
    public String marshal(UUID uuid)
    throws Exception {

        if (uuid != null) {

            return UUIDXmlAdapter.UUID_URN_PREFIX + uuid.toString();

        } else {

            return null;

        }

    }

    @Override
    public UUID unmarshal(String string)
    throws Exception {

        if (string.startsWith(UUIDXmlAdapter.UUID_URN_PREFIX)) {
            String uuidPart = string.substring(UUIDXmlAdapter.UUID_URN_PREFIX.length());
            return UUID.fromString(uuidPart);
        } else {
            throw new Exception("uuid attribute should start with " + UUIDXmlAdapter.UUID_URN_PREFIX);
        }
    }

}
