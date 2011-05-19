package org.jodaengine.ext.service;

import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;

/**
 * This interface provides a {@link Service} for deploying and managing {@link Extension}s
 * within the {@link JodaEngine}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public interface ExtensionService extends Service {
    
    /**
     * Returns whether the requested extension is available for the system.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return true in case the extension is available, false otherwise
     */
    <IExtension> boolean isExtensionAvailable(@Nonnull Class<IExtension> extension);
    
    /**
     * Returns any available extension for the specified extension point.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return an array of available extensions
     */
    @Nonnull <IExtension> List<IExtension> getExtensions(@Nonnull Class<IExtension> extension);
    
    /**
     * Returns an available extension for the specified extension point with the specified name.
     * 
     * @param <IExtensionService> the extension service's interface
     * @param extension the extension service's interface
     * @param name the extension's name
     * @return an array of available extensions
     * @throws ExtensionNotAvailableException no such extension available
     */
// CHECKSTYLE:OFF
    @Nonnull <IExtensionService extends Service> IExtensionService getExtensionService(@Nonnull Class<IExtensionService> extension,
                                                                                       @Nonnull String name)
    throws ExtensionNotAvailableException;
// CHECKSTYLE:ON
}
