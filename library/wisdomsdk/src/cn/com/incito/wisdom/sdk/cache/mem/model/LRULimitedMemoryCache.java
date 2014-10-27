package cn.com.incito.wisdom.sdk.cache.mem.model;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.com.incito.wisdom.sdk.cache.mem.LimitedMemoryCache;

/**
 * Limited {@link Object object} cache. Provides {@link Object object} storing. Size of all stored objects will not to
 * exceed size limit. When cache reaches limit size then the least recently used object is deleted from cache.
 * 
 */
public abstract class LRULimitedMemoryCache<K, V> extends LimitedMemoryCache<K, V> {

	private static final int INITIAL_CAPACITY = 10;
	private static final float LOAD_FACTOR = 1.1f;

	/** Cache providing Least-Recently-Used logic */
	private final Map<K, V> lruCache = Collections.synchronizedMap(new LinkedHashMap<K, V>(INITIAL_CAPACITY, LOAD_FACTOR, true));

	public LRULimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(K key, V value) {
		if (super.put(key, value)) {
			lruCache.put(key, value);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public V get(K key) {
		lruCache.get(key); // call "get" for LRU logic
		return super.get(key);
	}

	@Override
	public void remove(K key) {
		lruCache.remove(key);
		super.remove(key);
	}

	@Override
	public void clear() {
		lruCache.clear();
		super.clear();
	}


	@Override
	protected V removeNext() {
		V mostLongUsedValue = null;
		synchronized (lruCache) {
			Iterator<Entry<K, V>> it = lruCache.entrySet().iterator();
			if (it.hasNext()) {
				Entry<K, V> entry = it.next();
				mostLongUsedValue = entry.getValue();
				it.remove();
			}
		}
		return mostLongUsedValue;
	}

	@Override
	protected Reference<V> createReference(V value) {
		return new WeakReference<V>(value);
	}
}
