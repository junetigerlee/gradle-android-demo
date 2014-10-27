package cn.com.incito.wisdom.sdk.cache.disk.naming;

/**
 * Generates names for files at disc cache
 * 
 */
public interface FileNameGenerator {
	/** Generates unique file name for image defined by URI */
	public abstract String generate(String imageUri);
}
