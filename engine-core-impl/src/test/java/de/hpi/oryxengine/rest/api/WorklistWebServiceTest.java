package de.hpi.oryxengine.rest.api;

import de.hpi.oryxengine.rest.AbstractJsonServerTest;


/**
 * Tests the interaction with our WorklistWebService.
 */
public class WorklistWebServiceTest extends AbstractJsonServerTest {

    @Override
    protected Class<?> getResource() {

        return WorklistWebService.class;
    }
    

}
