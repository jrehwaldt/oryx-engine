package de.hpi.oryxengine.web;

/**
 * Container class for menu items.
 * 
 * @author Jan Rehwaldt
 */
public final class NavigationEntry {
    
    private final String path;
    private final String name;
    
    /**
     * Create a new entry.
     * 
     * @param path the web path
     * @param name the menu name
     */
    public NavigationEntry(String path,
                           String name) {
        this.path = path;
        this.name = name;
    }
    
    /**
     * Get the path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Get the menu name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
}
