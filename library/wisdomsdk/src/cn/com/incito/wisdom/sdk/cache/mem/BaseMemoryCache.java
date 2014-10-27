package cn.com.incito.wisdom.sdk.cache.mem;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Base memory cache. Implements common functionality for memory cache. Provides object references (
 * {@linkplain Reference not strong}) storing.
 * 
 */
public abstract class BaseMemoryCache<K, V> implements AbstractMemoryCache<K, V> {

	/** Stores not strong references to objects */
	private final Map<K, Reference<V>> softMap = Collections.synchronizedMap(new HashMap<K, Reference<V>>());

	@Override
	public V get(K key) {
		V result = null;
		Reference<V> reference = softMap.get(key);
		if (reference != null) {
			result = reference.get();
		}
		return result;
	}

	@Override
	public boolean put(K key, V value) {
		softMap.put(key, createReference(value));
		return true;
	}

	@Override
	public void remove(K key) {
		softMap.remove(key);
	}

	@Override
	public Collection<K> keys() {
	    synchronized (softMap) {
	        return new HashSet<K>(softMap.keySet());
	    }
	}

	@Override
	public void clear() {
		softMap.clear();
	}

	/** Creates {@linkplain Reference not strong} reference of value */
	protected abstract Reference<V> createReference(V value);
}
