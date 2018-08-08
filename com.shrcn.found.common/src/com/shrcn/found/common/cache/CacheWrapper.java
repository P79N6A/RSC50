/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.CacheElement;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-4-2
 */
/**
 * $Log$
 */
public class CacheWrapper {
	private JCS jcsCache = null; 
    
    public CacheWrapper(JCS cache){ 
        this.jcsCache = cache; 
    } 
            
    public void put(String key , Object value){ 
        try{ 
            jcsCache.put(key, value); 
        }catch(CacheException e){ 
        	throw new RuntimeException(e); 
        } 
    } 
            
    public Object get(String key){ 
        CacheElement cacheElement = (CacheElement) jcsCache.getCacheElement(key); 
        if (null != cacheElement) { 
        	return cacheElement.val; 
        } 
        return null; 
    } 

    public void clear() {
    	try {
			jcsCache.clear();
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
    }
    
    public void dispose() {
    	jcsCache.dispose();
    }

	public boolean containsKey(String key) {
		return get(key) != null;
	}

	public void remove(String key) {
		try {
			jcsCache.remove(key);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

}
