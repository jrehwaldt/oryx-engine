package de.hpi.oryxengine.process.definition;

import java.io.InputStream;
import java.util.UUID;

import de.hpi.oryxengine.util.io.StreamSource;

/**
 * The implementation of the model '{@link AbstractProcessArtifact}'.
 */
public class ProcessArtifact extends AbstractProcessArtifact {

    private UUID id;
    private String name;
    private StreamSource streamSource;

    /**
     * Default constructor for this model.
     */
    public ProcessArtifact(String name, StreamSource streamSource) {

        this.id = UUID.randomUUID();
        this.name = name;
        this.streamSource = streamSource;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public InputStream getInputStream() {

        return streamSource.getInputStream();
    }
}
