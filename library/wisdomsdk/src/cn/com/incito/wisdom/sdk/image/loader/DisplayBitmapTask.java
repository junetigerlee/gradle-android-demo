package cn.com.incito.wisdom.sdk.image.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;
import cn.com.incito.wisdom.sdk.image.display.BitmapDisplayer;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.image.loader.assist.LoadedFrom;

/**
 * Displays bitmap in {@link ImageView}. Must be called on UI thread.
 * 
 * @see ImageLoadingListener
 * @see BitmapDisplayer
 */
final class DisplayBitmapTask implements Runnable {

    private static final String LOG_DISPLAY_IMAGE_IN_IMAGEVIEW = "Display image in ImageView (loaded from %1$s) [%2$s]";
    private static final String LOG_TASK_CANCELLED = "ImageView is reused for another image. Task is cancelled. [%s]";
    
	private final Bitmap bitmap;
	private final String imageUri;
	private final ImageView imageView;
	private final String memoryCacheKey;
	private final BitmapDisplayer displayer;
	private final ImageLoadingListener listener;
	private final ImageLoaderEngine engine;
	private final LoadedFrom loadedFrom;

	private boolean loggingEnabled;
	private boolean isApplyAnim;
	public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine, boolean isApplyAnim, LoadedFrom loadedFrom) {
		this.bitmap = bitmap;
		imageUri = imageLoadingInfo.uri;
		imageView = imageLoadingInfo.imageView;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		displayer = imageLoadingInfo.options.getDisplayer();
		listener = imageLoadingInfo.listener;
		this.engine = engine;
		this.isApplyAnim = isApplyAnim;
		this.loadedFrom = loadedFrom;
	}

	public void run() {
		if (isViewWasReused()) {
			if (loggingEnabled) L.i(LOG_TASK_CANCELLED, memoryCacheKey);
			listener.onLoadingCancelled(imageUri, imageView);
		} else {
		    if (loggingEnabled) L.i(LOG_DISPLAY_IMAGE_IN_IMAGEVIEW, loadedFrom, memoryCacheKey);
			Bitmap displayedBitmap = displayer.display(bitmap, imageView, isApplyAnim, loadedFrom);
			listener.onLoadingComplete(imageUri, imageView, displayedBitmap);
			engine.cancelDisplayTaskFor(imageView);
		}
	}

	/** Checks whether memory cache key (image URI) for current ImageView is actual */
	private boolean isViewWasReused() {
		String currentCacheKey = engine.getLoadingUriForView(imageView);
		return !memoryCacheKey.equals(currentCacheKey);
	}

	void setLoggingEnabled(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}
}
