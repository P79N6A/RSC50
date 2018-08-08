package com.shrcn.found.common.cache.test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.shrcn.found.common.cache.CacheFactory;
import com.shrcn.found.common.cache.CacheWrapper;

public class CacheWrapperTest {
	
	private static final String cacheName = "modifyCache";

	@Test
	public void testPutValue() {
		CacheFactory.createHashMapWrapper(cacheName); 
		CacheWrapper xmCache = CacheFactory.getCacheWrapper(cacheName);
		xmCache.put("aa", "111");
		assertEquals("111", xmCache.get("aa"));
	}
}
