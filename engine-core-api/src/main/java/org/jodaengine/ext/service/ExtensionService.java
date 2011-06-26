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
     * Returns any available extension for the specified extension point as new instance.
     * 
     * Invoking this method <b>more then once</b> it will create a <b>new</b> instance each time.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return an array of extension instances
     */
    @Nonnull <IExtension> List<IExtension> getExtensions(@Nonnull Class<IExtension> extension);
    
    /**
     * Returns an available extension for the specified extension point with the specified name.
     * 
     * Invoking this method <b>more then once</b> it will continue to provide the <b>same</b> instance.
     * 
     * @param <IExtService> the extension service's interface
     * @param extension the extension service's interface
     * @param name the extension's name
     * @return an array of available extensions
     * @throws ExtensionNotAvailableException no such extension available
     */
    @Nonnull <IExtService extends Service> IExtService getServiceExtension(@Nonnull Class<IExtService> extension,
                                                                           @Nonnull String name)
    throws ExtensionNotAvailableException;
    
    /**
     * Returns any available web service extension instance.
     * 
     * Invoking this method <b>more then once</b> it will continue to provide the <b>same</b> instances.
     * 
     * @return an array of web extension instances
     */
    @Nonnull List<Service> getExtensionWebServiceSingletons();
    
    /**
     * Clears the intern extension database and reloads it.
     * 
     * If the provided extension is a running {@link Service} it will
     * NOT be stopped.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     */
    <IExtension> void rebuildExtensionDatabase(@Nonnull Class<IExtension> extension);
    
    /**
     * Returns any available extension type for the specified extension point.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return an array of available extensions types
     */
    @Nonnull <IExtension> List<Class<IExtension>> getExtensionTypes(@Nonnull Class<IExtension> extension);
}
