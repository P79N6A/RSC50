/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-4-2
 */
/**
 * $Log$
 */
public class CacheFactory {
    private static Map<String, CacheWrapper> hashMapWrapper = new HashMap<String, CacheWrapper>(); 
          
    /** 
     * 获取一个名称为cacheName的缓存对象；如果不存在，返回null 
     * @param cacheName 
     * @return 
     */
    public static CacheWrapper getCacheWrapper(String cacheName){ 
        return hashMapWrapper.get(cacheName); 
    } 
          
    /** 
     * 清理所有的缓存 
     */
    public static void clearCache(){ 
        Object[] cacheArray = hashMapWrapper.keySet().toArray(); 
        for(int i=0, l=cacheArray.length; i<l; i++){ 
            String cacheName = cacheArray[i].toString(); 
            CacheWrapper cacheWrapper = hashMapWrapper.get(cacheName); 
            cacheWrapper.clear(); 
        } 
    }
    
    public static void dispose(){ 
        Object[] cacheArray = hashMapWrapper.keySet().toArray(); 
        for(int i=0, l=cacheArray.length; i<l; i++){ 
            String cacheName = cacheArray[i].toString(); 
            CacheWrapper cacheWrapper = hashMapWrapper.get(cacheName); 
            cacheWrapper.dispose();
        } 
    } 
          
    /** 
     * 获取一个名称为cacheName的缓存对象；如果不存在，则创建一个新的缓存对象 
     * @param cacheName 
     * @return 
     */
    private static CacheWrapper createCacheWrapper(String cacheName){ 
        JCS cache = null; 
        try{ 
            cache = JCS.getInstance(cacheName);
            return new CacheWrapper(cache); 
        }catch(CacheException e){ 
            return null; 
        } 
    } 
          
    /** 
     * 创建缓存对象 
     * @param cacheName 
     */
    public static void createHashMapWrapper(String cacheName){ 
        hashMapWrapper.put(cacheName, createCacheWrapper(cacheName)); 
    } 
          
}
