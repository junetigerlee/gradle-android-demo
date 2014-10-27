package cn.com.incito.wisdom.sdk.cache.disk.impl;

import java.io.File;

import cn.com.incito.wisdom.sdk.cache.disk.DiskCacheConfigFactory;
import cn.com.incito.wisdom.sdk.cache.disk.LimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.naming.FileNameGenerator;

/**
 * Disc cache limited by file count. If file count in cache directory exceeds specified limit then file with the most
 * oldest last usage date will be deleted.
 * 
 * @see LimitedDiscCache
 */
public class FileCountLimitedDiscCache extends LimitedDiscCache {

	/**
	 * @param cacheDir
	 *            Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's needed
	 *            for right cache limit work.
	 * @param maxFileCount
	 *            Maximum file count for cache. If file count in cache directory exceeds this limit then file with the
	 *            most oldest last usage date will be deleted.
	 */
	public FileCountLimitedDiscCache(File cacheDir, int maxFileCount) {
		this(cacheDir, DiskCacheConfigFactory.createFileNameGenerator(), maxFileCount);
	}

	/**
	 * @param cacheDir
	 *            Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's needed
	 *            for right cache limit work.
	 * @param fileNameGenerator
	 *            Name generator for cached files
	 * @param maxFileCount
	 *            Maximum file count for cache. If file count in cache directory exceeds this limit then file with the
	 *            most oldest last usage date will be deleted.
	 */
	public FileCountLimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int maxFileCount) {
		super(cacheDir, fileNameGenerator, maxFileCount);
	}

	@Override
	protected int getSize(File file) {
		return 1;
	}
}
