package de.hpi.oryxengine.rest.api;

import org.testng.annotations.Test;

import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests our repository web service.
 */
public class RepositoryWebServiceTest extends AbstractJsonServerTest {

    @Override
    protected Class<?> getResource() {

        return RepositoryWebService.class;
    }

}
