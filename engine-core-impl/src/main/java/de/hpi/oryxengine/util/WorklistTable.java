package de.hpi.oryxengine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * 
 * @author Gery
 *
 * @param <K>
 * @param <V>
 */
public class WorklistTable<K, V extends List<WorklistItem>> extends HashMap<K, V> {

    private static final long serialVersionUID = 5188122055091014670L;

    @Override
    public V get(Object key) {

        K castedKey = (K) key;
        
        V worklistItems = super.get(castedKey);
        if (worklistItems == null) {
            V emptyWorklistItems  = (V) new ArrayList<WorklistItem>();
            return emptyWorklistItems;
        }
        
        return worklistItems;
    }
    
    public void addWorklistItemTo(K key, WorklistItem worklistItem) {
        List<WorklistItem> worklistItems = get(key);
        
        worklistItems.add(worklistItem);
        
        if (worklistItems.size() == 1) {
            
            V castedWorklistItems = (V) worklistItems;
            put(key, castedWorklistItems);
        }
    }
}
