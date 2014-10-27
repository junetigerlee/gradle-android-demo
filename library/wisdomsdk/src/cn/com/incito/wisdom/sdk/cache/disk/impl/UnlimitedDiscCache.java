package cn.com.incito.wisdom.sdk.cache.disk.impl;

import java.io.File;

import cn.com.incito.wisdom.sdk.cache.disk.AbstractDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.BaseDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.DiskCacheConfigFactory;
import cn.com.incito.wisdom.sdk.cache.disk.naming.FileNameGenerator;

/**
 * Default implementation of {@linkplain AbstractDiscCache disc cache}. Cache size is unlimited.
 * 
 * @see BaseDiscCache
 */
public class UnlimitedDiscCache extends BaseDiscCache {
	/**
	 * @param cacheDir
	 *            Directory for file caching
	 */
	public UnlimitedDiscCache(File cacheDir) {
		this(cacheDir, DiskCacheConfigFactory.createFileNameGenerator());
	}

	/**
	 * @param cacheDir
	 *            Directory for file caching
	 * @param fileNameGenerator
	 *            Name generator for cached files
	 */
	public UnlimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		super(cacheDir, fileNameGenerator);
	}

	@Override
	public void put(String key, File file) {
		// Do nothing
	}
}
