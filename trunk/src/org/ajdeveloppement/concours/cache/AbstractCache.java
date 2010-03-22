package org.ajdeveloppement.concours.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<UID, CT> {
	protected Map<UID,SoftReference<CT>> instanceCache = Collections.synchronizedMap(
			new HashMap<UID,SoftReference<CT>>());
	
	public abstract void add(CT object);
	
	public CT get(UID uniqueObjectId) {
		if(instanceCache.containsKey(uniqueObjectId))
			return instanceCache.get(uniqueObjectId).get();
		return null;
	}
	
	public void remove(UID uniqueObjectId) {
		instanceCache.remove(uniqueObjectId);
	}
	
	public boolean containsKey(UID uniqueObjectId) {
		return instanceCache.containsKey(uniqueObjectId);
	}
}
