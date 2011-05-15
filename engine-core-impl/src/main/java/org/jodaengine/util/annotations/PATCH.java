package org.jodaengine.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

/**
 * This annotation provides the HTTP Verb Patch to the REST API, as it is not included in javax.ws.rs.
 * A use case: Partial Updates of collections, that do not fit into the schemas of PUT, POST or DELETE.
 * 
 * @see http://tools.ietf.org/html/rfc5789
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {

}
