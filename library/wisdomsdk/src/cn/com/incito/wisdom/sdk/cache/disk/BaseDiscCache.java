package cn.com.incito.wisdom.sdk.cache.disk;

import java.io.File;

import cn.com.incito.wisdom.sdk.cache.disk.naming.FileNameGenerator;

/**
 * Base disc cache. Implements common functionality for disc cache.
 * 
 * @see DiscCacheAware
 * @see FileNameGenerator
 */
public abstract class BaseDiscCache implements AbstractDiscCache {

    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";

    protected File cacheDir;

	private FileNameGenerator fileNameGenerator;

	public BaseDiscCache(File cacheDir) {
		this(cacheDir, DiskCacheConfigFactory.createFileNameGenerator());
	}

	public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
	    if (cacheDir == null) {
	        throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
	    }
	    if (fileNameGenerator == null) {
	        throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
	    }
		this.cacheDir = cacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}

	@Override
	public File get(String key) {
		String fileName = fileNameGenerator.generate(key);
		return new File(cacheDir, fileName);
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}
}