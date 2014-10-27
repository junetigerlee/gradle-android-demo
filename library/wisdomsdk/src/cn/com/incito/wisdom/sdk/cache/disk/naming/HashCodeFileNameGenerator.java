package cn.com.incito.wisdom.sdk.cache.disk.naming;

/**
 * Names file as URI {@linkplain String#hashCode() hashcode}
 * 
 */
public class HashCodeFileNameGenerator implements FileNameGenerator {
	@Override
	public String generate(String uri) {
		return String.valueOf(uri.hashCode());
	}
}
