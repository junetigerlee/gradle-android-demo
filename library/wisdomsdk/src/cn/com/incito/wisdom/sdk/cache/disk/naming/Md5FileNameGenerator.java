package cn.com.incito.wisdom.sdk.cache.disk.naming;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Names file as MD5 hash of URI
 * 
 */
public class Md5FileNameGenerator implements FileNameGenerator {

	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	@Override
	public String generate(String uri) {
		byte[] md5 = getMD5(uri.getBytes());
		BigInteger bi = new BigInteger(md5).abs();
		return bi.toString(RADIX);
	}

	private byte[] getMD5(byte[] data) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		} catch (NoSuchAlgorithmException e) {
//			WLog.e(Md5FileNameGenerator.class, e.getMessage(), e);
		}
		return hash;
	}
}
