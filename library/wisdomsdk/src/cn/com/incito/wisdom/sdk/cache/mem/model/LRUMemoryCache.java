package cn.com.incito.wisdom.sdk.cache.mem.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;

/**
 * A cache that holds strong references to a limited number of Values. Each time a value is accessed, it is moved to
 * the head of a queue. When a value is added to a full cache, the value at the end of that queue is evicted and may
 * become eligible for garbage collection.<br />
 * <br />
 * <b>NOTE:</b> This cache uses only strong references for stored Values.
 *
 */
public abstract class LRUMemoryCache<K, V> implements AbstractMemoryCache<K, V> {

	private final LinkedHashMap<K, V> map;

	private final int maxSize;
	/** Size of this cache in bytes */
	private int size;

	/**
	 * @param maxSize Maximum sum of the sizes of the Values in this cache
	 */
	public LRUMemoryCache(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
	}

	/**
	 * Returns the value for {@code key} if it exists in the cache. If a value was returned, it is moved to the head
	 * of the queue. This returns null if a value is not cached.
	 */
	@Override
	public final V get(K key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			return map.get(key);
		}
	}

	/**
	 * Caches {@code value} for {@code key}. The value is moved to the head of the queue.
	 */
	@Override
	public final boolean put(K key, V value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		}

		synchronized (this) {
			size += getSize(value);
			V previous = map.put(key, value);
			if (previous != null) {
				size -= getSize(previous);
			}
		}

		trimToSize(maxSize);
		return true;
	}

	/**
	 * Remove the eldest entries until the total of remaining entries is at or below the requested size.
	 * 
	 * @param maxSize the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
	 */
	private void trimToSize(int maxSize) {
		while (true) {
			K key;
			V value;
			synchronized (this) {
				if (size < 0 || (map.isEmpty() && size != 0)) {
					throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
				}

				if (size <= maxSize || map.isEmpty()) {
					break;
				}

				Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
				if (toEvict == null) {
					break;
				}
				key = toEvict.getKey();
				value = toEvict.getValue();
				map.remove(key);
				size -= getSize(value);
			}
		}
	}

	/** Removes the entry for {@code key} if it exists. */
	@Override
	public final void remove(K key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			V previous = map.remove(key);
			if (previous != null) {
				size -= getSize(previous);
			}
		}
	}

	@Override
	public Collection<K> keys() {
	    synchronized (this) {
	        return new HashSet<K>(map.keySet());
	    }
	}

	@Override
	public void clear() {
		trimToSize(-1); // -1 will evict 0-sized elements
	}

	protected abstract int getSize(V value);

	@Override
	public synchronized final String toString() {
		return String.format("LruCache[maxSize=%d]", maxSize);
	}
}