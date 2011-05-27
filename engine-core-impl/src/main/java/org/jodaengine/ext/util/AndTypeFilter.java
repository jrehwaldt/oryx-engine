package org.jodaengine.ext.util;

import java.io.IOException;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * A spring-based {@link TypeFilter}, which combines several provided filter and evaluates all of them true.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class AndTypeFilter implements TypeFilter {
    
    private TypeFilter[] filters;
    
    /**
     * Default constructor.
     * 
     * @param filters all filters to evaluate
     */
    public AndTypeFilter(TypeFilter ... filters) {
        this.filters = filters;
    }
    
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
    throws IOException {
        
        for (TypeFilter filter: this.filters) {
            if (!filter.match(metadataReader, metadataReaderFactory)) {
                return false;
            }
        }
        
        return true;
    }

}
