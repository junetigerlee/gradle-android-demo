package cn.com.incito.wisdom.sdk.image.loader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;
import cn.com.incito.wisdom.sdk.cache.disk.AbstractDiscCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageScaleType;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageSize;
import cn.com.incito.wisdom.sdk.image.loader.assist.LoadedFrom;
import cn.com.incito.wisdom.sdk.image.loader.assist.ViewScaleType;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason.FailType;
import cn.com.incito.wisdom.sdk.image.loader.decoder.ImageDecoder;
import cn.com.incito.wisdom.sdk.image.loader.decoder.ImageDecodingInfo;
import cn.com.incito.wisdom.sdk.net.download.ImageDownloader;
import cn.com.incito.wisdom.sdk.net.download.ImageDownloader.Scheme;
import cn.com.incito.wisdom.sdk.utils.FileUtils;

/**
 * Presents load'n'display image task. Used to load image from Internet or file system, decode it to {@link Bitmap}, and
 * display it in {@link ImageView} through {@link DisplayBitmapTask}.
 *
 * @see ImageLoaderConfiguration
 * @see ImageLoadingInfo
 */
final class LoadAndDisplayImageTask implements Runnable {


    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_TASK_CANCELLED = "ImageView is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";

    private static final String WARNING_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String WARNING_POST_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    
	private static final int BUFFER_SIZE = 8 * 1024; // 8 Kb

	private final ImageLoaderEngine engine;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	// Helper references
	private final ImageLoaderConfiguration configuration;
	private final ImageDownloader downloader;
	private final ImageDownloader networkDeniedDownloader;
	private final ImageDownloader slowNetworkDownloader;
	private final ImageDecoder decoder;
	private final boolean loggingEnabled;
	final String uri;
	private final String memoryCacheKey;
	final ImageView imageView;
	private final ImageSize targetSize;
	final DisplayImageOptions options;
	final ImageLoadingListener listener;
	private boolean isApplyAnim = false;
	private LoadedFrom loadedFrom = LoadedFrom.NETWORK;

	public LoadAndDisplayImageTask(ImageLoaderEngine engine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.engine = engine;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;

		configuration = engine.configuration;
		downloader = configuration.downloader;
		networkDeniedDownloader = configuration.networkDeniedDownloader;
		slowNetworkDownloader = configuration.slowNetworkDownloader;
		decoder = configuration.decoder;
		loggingEnabled = configuration.loggingEnabled;
		uri = imageLoadingInfo.uri;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		imageView = imageLoadingInfo.imageView;
		targetSize = imageLoadingInfo.targetSize;
		options = imageLoadingInfo.options;
		listener = imageLoadingInfo.listener;
	}

	@Override
	public void run() {
	    if (waitIfPaused()) return;
	    if (delayIfNeed()) return;

		ReentrantLock loadFromUriLock = imageLoadingInfo.loadFromUriLock;
		log(LOG_START_DISPLAY_IMAGE_TASK);
		if (loadFromUriLock.isLocked()) {
			log(LOG_WAITING_FOR_IMAGE_LOADED);
		}

		loadFromUriLock.lock();
		Bitmap bmp;
		try {
			if (checkTaskIsNotActual()) return;

			bmp = configuration.memoryCache.get(memoryCacheKey);
			if (bmp == null) {
				bmp = tryLoadBitmap();
				if (bmp == null) return;

				if (checkTaskIsNotActual() || checkTaskIsInterrupted()) return;

				if (options.shouldPreProcess()) {
					log(LOG_PREPROCESS_IMAGE);
					bmp = options.getPreProcessor().process(bmp);
					if (bmp == null) {
					    L.w(WARNING_PRE_PROCESSOR_NULL, memoryCacheKey);
					}
				}
				if (bmp != null && options.isCacheInMemory()) {
					log(LOG_CACHE_IMAGE_IN_MEMORY);
					configuration.memoryCache.put(memoryCacheKey, bmp);
				}
			} else {
			    loadedFrom = LoadedFrom.MEMORY_CACHE;
				log(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING);
			}

			if (bmp != null && options.shouldPostProcess()) {
				log(LOG_POSTPROCESS_IMAGE);
				bmp = options.getPostProcessor().process(bmp);
				if (bmp == null) {
				    L.w(WARNING_POST_PROCESSOR_NULL, memoryCacheKey);
				}
			}
		} finally {
			loadFromUriLock.unlock();
		}

		if (checkTaskIsNotActual() || checkTaskIsInterrupted()) return;

		DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, imageLoadingInfo, engine, isApplyAnim, loadedFrom);
		displayBitmapTask.setLoggingEnabled(loggingEnabled);
		handler.post(displayBitmapTask);
	}

	/**
	 * Check whether the image URI of this task matches to image URI which is actual for current ImageView at this
	 * moment and fire {@link ImageLoadingListener#onLoadingCancelled()} event if it doesn't.
	 */
	private boolean checkTaskIsNotActual() {
		String currentCacheKey = engine.getLoadingUriForView(imageView);
		// Check whether memory cache key (image URI) for current ImageView is actual. 
		// If ImageView is reused for another task then current task should be cancelled.
		boolean imageViewWasReused = !memoryCacheKey.equals(currentCacheKey);
		if (imageViewWasReused) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					listener.onLoadingCancelled(uri, imageView);
				}
			});
			log(LOG_TASK_CANCELLED);
		}
		return imageViewWasReused;
	}

	/**
	 * @return true - if task should be interrupted; false - otherwise
	 */
	private boolean waitIfPaused() {
	    AtomicBoolean pause = engine.getPause();
	    if (pause.get()) {
	        synchronized (pause) {
	            log(LOG_WAITING_FOR_RESUME);
	            try {
	                pause.wait();
	            } catch (InterruptedException e) {
	                L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
	                return true;
	            }
	            log(LOG_RESUME_AFTER_PAUSE);
	        }
	    }
	    return checkTaskIsNotActual();
	}
	
	/**
	 * @return true - if task should be interrupted; false - otherwise
	 */
	private boolean delayIfNeed() {
	    if (options.shouldDelayBeforeLoading()) {
	        log(LOG_DELAY_BEFORE_LOADING, options.getDelayBeforeLoading(), memoryCacheKey);
	        try {
	            Thread.sleep(options.getDelayBeforeLoading());
	        } catch (InterruptedException e) {
	            L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
	            return true;
	        }
	        return checkTaskIsNotActual();
	    }
	    return false;
	}

	/** Check whether the current task was interrupted */
	private boolean checkTaskIsInterrupted() {
		boolean interrupted = Thread.interrupted();
		if (interrupted) log(LOG_TASK_INTERRUPTED);
		return interrupted;
	}

	private Bitmap tryLoadBitmap() {
		AbstractDiscCache discCache = configuration.discCache;
		File imageFile = discCache.get(uri);
		File cacheDir = imageFile.getParentFile();
		if (cacheDir == null || (!cacheDir.exists() && !cacheDir.mkdirs())) {
			imageFile = configuration.reserveDiscCache.get(uri);
		}

		Bitmap bitmap = null;
		try {
			if (imageFile.exists()) {
				log(LOG_LOAD_IMAGE_FROM_DISC_CACHE);
				loadedFrom = LoadedFrom.DISC_CACHE;
				bitmap = decodeImage(Scheme.FILE.wrap(imageFile.getAbsolutePath()));
			}
			if (bitmap == null) {
			    log(LOG_LOAD_IMAGE_FROM_NETWORK);
			    loadedFrom = LoadedFrom.NETWORK;
			    String imageUriForDecoding = options.isCacheOnDisc() ? tryCacheImageOnDisc(imageFile) : uri;
			    if (!checkTaskIsNotActual()) {
			        bitmap = decodeImage(imageUriForDecoding);
	                if (bitmap == null) {
	                    fireImageLoadingFailedEvent(FailType.DECODING_ERROR, null);
	                }
			    }
			}
		} catch (IllegalStateException e) {
			fireImageLoadingFailedEvent(FailType.NETWORK_DENIED, null);
		} catch (IOException e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailType.IO_ERROR, e);
			if (imageFile.exists()) {
				imageFile.delete();
			}
		} catch (OutOfMemoryError e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailType.OUT_OF_MEMORY, e);
		} catch (Throwable e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailType.UNKNOWN, e);
		}
		return bitmap;
	}

	@SuppressWarnings("unused")
    private File getImageFileInDiscCache() {
	    AbstractDiscCache discCache = configuration.discCache;
	    File imageFile = discCache.get(uri);
	    File cacheDir = imageFile.getParentFile();
	    if (cacheDir == null || (!cacheDir.exists() && !cacheDir.mkdirs())) {
	        imageFile = configuration.reserveDiscCache.get(uri);
	        cacheDir = imageFile.getParentFile();
	        if (cacheDir != null && !cacheDir.exists()) {
	            cacheDir.mkdirs();
	        }
	    }
	    return imageFile;
	}	
	private Bitmap decodeImage(String imageUri) throws IOException {
		ViewScaleType viewScaleType = ViewScaleType.fromImageView(imageView);
		ImageDecodingInfo decodingInfo = new ImageDecodingInfo(memoryCacheKey, imageUri, targetSize, viewScaleType, getDownloader(), options);
		return decoder.decode(decodingInfo);
	}

	/**
	 * @return Cached image URI; or original image URI if caching failed
	 */
	private String tryCacheImageOnDisc(File targetFile) {
	    log(LOG_CACHE_IMAGE_ON_DISC);
	    try {
	        int width = configuration.maxImageWidthForDiscCache;
	        int height = configuration.maxImageHeightForDiscCache;
	        boolean saved = false;
	        if (width > 0 || height > 0) {
	            saved = downloadSizedImage(targetFile, width, height);
	        }
	        if (!saved) {
	            downloadImage(targetFile);
	        }

	        configuration.discCache.put(uri, targetFile);
	        return Scheme.FILE.wrap(targetFile.getAbsolutePath());
	    } catch (IOException e) {
	        L.e(e);
	        return uri;
	    }
	}
	
	private boolean downloadSizedImage(File targetFile, int maxWidth, int maxHeight) throws IOException {
	    // Download, decode, compress and save image
	    ImageSize targetImageSize = new ImageSize(maxWidth, maxHeight);
	    DisplayImageOptions specialOptions = new DisplayImageOptions.Builder().cloneFrom(options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	    ImageDecodingInfo decodingInfo = new ImageDecodingInfo(memoryCacheKey, uri, targetImageSize, ViewScaleType.FIT_INSIDE, getDownloader(), specialOptions);
	    Bitmap bmp = decoder.decode(decodingInfo);
	    boolean savedSuccessfully = false;
	    if (bmp != null) {
	        OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
	        try {
	            savedSuccessfully = bmp.compress(configuration.imageCompressFormatForDiscCache, configuration.imageQualityForDiscCache, os);
	        } finally {
	            FileUtils.closeSilently(os);
	        }
	        if (savedSuccessfully) {
	            bmp.recycle();
	        }
	    }
	    return savedSuccessfully;
	}

	private void downloadImage(File targetFile) throws IOException {
		InputStream is = getDownloader().getStream(uri, options.getExtraForDownloader());
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			try {
				FileUtils.copyStream(is, os);
			} finally {
			    FileUtils.closeSilently(os);
			}
		} finally {
			FileUtils.closeSilently(is);
			isApplyAnim = true;
		}
	}

	private void fireImageLoadingFailedEvent(final FailType failType, final Throwable failCause) {
		if (!Thread.interrupted()) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (options.shouldShowImageOnFail()) {
						imageView.setImageResource(options.getImageOnFail());
					}
					listener.onLoadingFailed(uri, imageView, new FailReason(failType, failCause));
				}
			});
		}
	}

	private ImageDownloader getDownloader() {
		ImageDownloader d;
		if (engine.isNetworkDenied()) {
			d = networkDeniedDownloader;
		} else if (engine.isSlowNetwork()) {
			d = slowNetworkDownloader;
		} else {
			d = downloader;
		}
		return d;
	}

	String getLoadingUri() {
		return uri;
	}

	private void log(String message) {
	    if (loggingEnabled) L.i(message, memoryCacheKey);
	}
	
	private void log(String message, Object... args) {
		if (loggingEnabled) L.i(message, args);
	}
}
