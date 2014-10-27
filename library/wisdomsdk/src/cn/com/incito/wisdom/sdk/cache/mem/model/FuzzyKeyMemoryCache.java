package cn.com.incito.wisdom.sdk.cache.mem.model;

import java.util.Collection;
import java.util.Comparator;

import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;

/**
 * Decorator for {@link AbstractMemoryCache}. Provides special feature for cache: some different keys are considered as
 * equals (using {@link Comparator comparator}). And when you try to put some value into cache by key so entries with
 * "equals" keys will be removed from cache before.<br />
 * <b>NOTE:</b> Used for internal needs. Normally you don't need to use this class.
 * 
 */
public class FuzzyKeyMemoryCache<K, V> implements AbstractMemoryCache<K, V> {

	private final AbstractMemoryCache<K, V> cache;
	private final Comparator<K> keyComparator;

	public FuzzyKeyMemoryCache(AbstractMemoryCache<K, V> cache, Comparator<K> keyComparator) {
		this.cache = cache;
		this.keyComparator = keyComparator;
	}

	@Override
	public boolean put(K key, V value) {
		// Search equal key and remove this entry
		synchronized (cache) {
		    K keyToRemove = null;
		    for (K cacheKey : cache.keys()) {
		        if (keyComparator.compare(key, cacheKey) == 0) {
		            keyToRemove = cacheKey;
		            break;
		        }
		    }
		    if (keyToRemove != null) {
		        cache.remove(keyToRemove);
		    }
		}
		return cache.put(key, value);
	}

	@Override
	public V get(K key) {
		return cache.get(key);
	}

	@Override
	public void remove(K key) {
		cache.remove(key);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Collection<K> keys() {
		return cache.keys();
	}
}
