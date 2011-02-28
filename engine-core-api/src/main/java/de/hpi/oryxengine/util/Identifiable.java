package de.hpi.oryxengine.util;

import java.util.UUID;

/**
 * This interface declares certain entities as identifiable
 * and therefore requires them to provide a getter for it's ID.
 * 
 * @author Jan Rehwaldt
 */
public interface Identifiable {
    UUID getID();
}
