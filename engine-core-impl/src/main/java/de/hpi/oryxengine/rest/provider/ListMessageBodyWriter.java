package de.hpi.oryxengine.rest.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This Class is used to use Gson to write lists with worklist items.
 */
//@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ListMessageBodyWriter implements MessageBodyWriter<List<?>> {

    @Override
    public boolean isWriteable(Class<?> type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType) {
        
        return (List.class.isAssignableFrom(type));
    }

    @Override
    public long getSize(List<?> t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        
        return -1;
    }
    
    @Override
    public void writeTo(List<?> t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream)
    throws IOException {
        
        Gson gson = new GsonBuilder().serializeNulls().create();
        String gsonString = gson.toJson(t, List.class);
        OutputStreamWriter ow = new OutputStreamWriter(entityStream);
        ow.write(gsonString);
        ow.close();
    }
}
