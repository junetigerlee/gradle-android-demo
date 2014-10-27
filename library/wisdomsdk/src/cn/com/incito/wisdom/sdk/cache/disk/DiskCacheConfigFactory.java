package cn.com.incito.wisdom.sdk.cache.disk;

import cn.com.incito.wisdom.sdk.cache.disk.naming.FileNameGenerator;
import cn.com.incito.wisdom.sdk.cache.disk.naming.HashCodeFileNameGenerator;

public class DiskCacheConfigFactory {

	/** Create {@linkplain HashCodeFileNameGenerator default implementation} of FileNameGenerator */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}
}
