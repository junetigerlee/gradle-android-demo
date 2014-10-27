package cn.com.incito.wisdom.sdk.cache.mem.model;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import cn.com.incito.wisdom.sdk.cache.mem.BaseMemoryCache;

/**
 * Memory cache with {@linkplain WeakReference weak references} to {@linkplain android.lang.Object object}
 * 
 */
public class WeakMemoryCache<K, V> extends BaseMemoryCache<K, V> {
	@Override
	protected Reference<V> createReference(V value) {
		return new WeakReference<V>(value);
	}
}
