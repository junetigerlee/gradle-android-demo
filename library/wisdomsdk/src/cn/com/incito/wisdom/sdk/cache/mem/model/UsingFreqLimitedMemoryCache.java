package cn.com.incito.wisdom.sdk.cache.mem.model;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.incito.wisdom.sdk.cache.mem.LimitedMemoryCache;

/**
 * Limited {@link Object object} cache. Provides {@link Object object} storing. Size of all stored objects will not to
 * exceed size limit. When cache reaches limit size then the bitmap which used the least frequently is deleted from
 * cache.
 * 
 */
public abstract class UsingFreqLimitedMemoryCache<K, V> extends LimitedMemoryCache<K, V> {

	/**
	 * Contains strong references to stored objects (keys) and last object usage date (in milliseconds). If hard cache
	 * size will exceed limit then object with the least frequently usage is deleted (but it continue exist at
	 * {@link #softMap} and can be collected by GC at any time)
	 */
	private final Map<V, Integer> usingCounts = Collections.synchronizedMap(new HashMap<V, Integer>());

	public UsingFreqLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(K key, V value) {
		if (super.put(key, value)) {
			usingCounts.put(value, 0);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public V get(K key) {
		V value = super.get(key);
		// Increment usage count for value if value is contained in hardCahe
		if (value != null) {
			Integer usageCount = usingCounts.get(value);
			if (usageCount != null) {
				usingCounts.put(value, usageCount + 1);
			}
		}
		return value;
	}

	@Override
	public void remove(K key) {
		V value = super.get(key);
		if (value != null) {
			usingCounts.remove(value);
		}
		super.remove(key);
	}

	@Override
	public void clear() {
		usingCounts.clear();
		super.clear();
	}

	@Override
	protected V removeNext() {
		Integer minUsageCount = null;
		V leastUsedValue = null;
		Set<Entry<V, Integer>> entries = usingCounts.entrySet();
		synchronized (usingCounts) {
			for (Entry<V, Integer> entry : entries) {
				if (leastUsedValue == null) {
					leastUsedValue = entry.getKey();
					minUsageCount = entry.getValue();
				} else {
					Integer lastValueUsage = entry.getValue();
					if (lastValueUsage < minUsageCount) {
						minUsageCount = lastValueUsage;
						leastUsedValue = entry.getKey();
					}
				}
			}
		}
		usingCounts.remove(leastUsedValue);
		return leastUsedValue;
	}

	@Override
	protected Reference<V> createReference(V value) {
		return new WeakReference<V>(value);
	}
}
