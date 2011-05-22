package org.jodaengine.ext;

import java.util.Observer;

import org.jodaengine.plugin.Plugin;

/**
 * This interface defines, that our implementation relies
 * on the Java observer implementation. 
 */
public interface ObserverListener
extends Plugin, Observer {
    
}
