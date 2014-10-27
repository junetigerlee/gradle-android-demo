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
 * Limited {@link Objects object} cache. Provides {@link Objects object} storing. Size of all stored objects will not to
 * exceed size limit. When cache reaches limit size then the objects which used the least frequently is deleted from
 * cache.
 * 
 */
public abstract class LargestLimitedMemoryCache<K, V> extends LimitedMemoryCache<K, V> {

	/**
	 * Contains strong references to stored objects (keys) and last object usage date (in milliseconds). If hard cache
	 * size will exceed limit then object with the least frequently usage is deleted (but it continue exist at
	 * {@link #softMap} and can be collected by GC at any time)
	 */
	private final Map<V, Integer> valueSizes = Collections.synchronizedMap(new HashMap<V, Integer>());

	public LargestLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(K key, V value) {
		if (super.put(key, value)) {
			valueSizes.put(value, getSize(value));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void remove(K key) {
		V value = super.get(key);
		if (value != null) {
			valueSizes.remove(value);
		}
		super.remove(key);
	}

	@Override
	public void clear() {
		valueSizes.clear();
		super.clear();
	}

	@Override
	protected V removeNext() {
		Integer maxSize = null;
		V largestValue = null;
		Set<Entry<V, Integer>> entries = valueSizes.entrySet();
		synchronized (valueSizes) {
			for (Entry<V, Integer> entry : entries) {
				if (largestValue == null) {
					largestValue = entry.getKey();
					maxSize = entry.getValue();
				} else {
					Integer size = entry.getValue();
					if (size > maxSize) {
						maxSize = size;
						largestValue = entry.getKey();
					}
				}
			}
		}
		valueSizes.remove(largestValue);
		return largestValue;
	}

	@Override
	protected Reference<V> createReference(V value) {
		return new WeakReference<V>(value);
	}
}
