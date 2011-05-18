package org.jodaengine.process.definition;

import java.io.InputStream;
import java.util.UUID;

import org.jodaengine.util.io.StreamSource;


/**
 * The implementation of the model '{@link AbstractProcessArtifact}'.
 */
public class ProcessArtifact extends AbstractProcessArtifact {

    private String id;
    private StreamSource streamSource;

    /**
     * Default constructor for this model.
     */
    public ProcessArtifact(String name, StreamSource streamSource) {

        this.id = name;
        this.streamSource = streamSource;
    }

    @Override
    public String getID() {

        return id;
    }

    @Override
    public InputStream getInputStream() {

        return streamSource.getInputStream();
    }
}
