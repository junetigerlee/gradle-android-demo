package cn.com.incito.wisdom.sdk.image.loader.assist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoaderConfiguration;

/**
 * Utility for generating of keys for memory cache, key comparing and other work with memory cache
 * 
 */
public final class MemoryCacheUtil {

	private static final String URI_AND_SIZE_SEPARATOR = "_";
	private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

	private MemoryCacheUtil() {
	}

	/**
	 * Generates key for memory cache for incoming image (URI + size).<br />
	 * Pattern for cache key - {@value #MEMORY_CACHE_KEY_FORMAT}, where (1) - image URI, (2) - image width, (3) - image
	 * height.
	 */
	public static String generateKey(String imageUri, ImageSize targetSize) {
		return new StringBuilder(imageUri).append(URI_AND_SIZE_SEPARATOR).append(targetSize.getWidth()).append(WIDTH_AND_HEIGHT_SEPARATOR).append(targetSize.getHeight()).toString();
	}

	public static Comparator<String> createFuzzyKeyComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String key1, String key2) {
				String imageUri1 = key1.substring(0, key1.lastIndexOf(URI_AND_SIZE_SEPARATOR));
				String imageUri2 = key2.substring(0, key2.lastIndexOf(URI_AND_SIZE_SEPARATOR));
				return imageUri1.compareTo(imageUri2);
			}
		};
	}

	/**
	 * Searches all bitmaps in memory cache which are corresponded to incoming URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same image if only you didn't set
	 * {@link ImageLoaderConfiguration.Builder#denyCacheImageMultipleSizesInMemory()
	 * denyCacheImageMultipleSizesInMemory()} option in {@linkplain ImageLoaderConfiguration configuration}
	 */
	public static List<Bitmap> findCachedBitmapsForImageUri(String imageUri, AbstractMemoryCache<String, Bitmap> memoryCache) {
		List<Bitmap> values = new ArrayList<Bitmap>();
		for (String key : memoryCache.keys()) {
			if (key.startsWith(imageUri)) {
				values.add(memoryCache.get(key));
			}
		}
		return values;
	}

	/**
	 * Searches all keys in memory cache which are corresponded to incoming URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same image if only you didn't set
	 * {@link ImageLoaderConfiguration.Builder#denyCacheImageMultipleSizesInMemory()
	 * denyCacheImageMultipleSizesInMemory()} option in {@linkplain ImageLoaderConfiguration configuration}
	 */
	public static List<String> findCacheKeysForImageUri(String imageUri, AbstractMemoryCache<String, Bitmap> memoryCache) {
		List<String> values = new ArrayList<String>();
		for (String key : memoryCache.keys()) {
			if (key.startsWith(imageUri)) {
				values.add(key);
			}
		}
		return values;
	}

	/**
	 * Removes from memory cache all images for incoming URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same image if only you didn't set
	 * {@link ImageLoaderConfiguration.Builder#denyCacheImageMultipleSizesInMemory()
	 * denyCacheImageMultipleSizesInMemory()} option in {@linkplain ImageLoaderConfiguration configuration}
	 */
	public static void removeFromCache(String imageUri, AbstractMemoryCache<String, Bitmap> memoryCache) {
		List<String> keysToRemove = new ArrayList<String>();
		for (String key : memoryCache.keys()) {
			if (key.startsWith(imageUri)) {
				keysToRemove.add(key);
			}
		}
		for (String keyToRemove : keysToRemove) {
			memoryCache.remove(keyToRemove);
		}
	}
}
