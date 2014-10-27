package cn.com.incito.wisdom.sdk.image.loader;

import java.util.concurrent.locks.ReentrantLock;

import android.widget.ImageView;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageSize;
import cn.com.incito.wisdom.sdk.image.loader.assist.MemoryCacheUtil;

/**
 * Information for load'n'display image task
 * 
 * @see MemoryCacheUtil
 * @see DisplayImageOptions
 * @see ImageLoadingListener
 */
final class ImageLoadingInfo {

	final String uri;
	final String memoryCacheKey;
	final ImageView imageView;
	final ImageSize targetSize;
	final DisplayImageOptions options;
	final ImageLoadingListener listener;
	final ReentrantLock loadFromUriLock;

	public ImageLoadingInfo(String uri, ImageView imageView, ImageSize targetSize, String memoryCacheKey, DisplayImageOptions options, ImageLoadingListener listener, ReentrantLock loadFromUriLock) {
		this.uri = uri;
		this.imageView = imageView;
		this.targetSize = targetSize;
		this.options = options;
		this.listener = listener;
		this.loadFromUriLock = loadFromUriLock;
		this.memoryCacheKey = memoryCacheKey;
	}
}
