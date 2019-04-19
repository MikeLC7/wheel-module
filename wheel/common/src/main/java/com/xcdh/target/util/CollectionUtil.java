package com.xcdh.target.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtil {
	
	private CollectionUtil() {
		// disallow initialization
	}
	
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

}
