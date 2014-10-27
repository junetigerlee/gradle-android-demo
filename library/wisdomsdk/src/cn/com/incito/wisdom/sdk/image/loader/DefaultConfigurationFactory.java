package cn.com.incito.wisdom.sdk.image.loader;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import cn.com.incito.wisdom.sdk.cache.disk.AbstractDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.impl.FileCountLimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.impl.TotalSizeLimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.impl.UnlimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.naming.FileNameGenerator;
import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;
import cn.com.incito.wisdom.sdk.cache.mem.model.LRULimitedMemoryCache;
import cn.com.incito.wisdom.sdk.datastructure.deque.LIFOLinkedBlockingDeque;
import cn.com.incito.wisdom.sdk.image.display.BitmapDisplayer;
import cn.com.incito.wisdom.sdk.image.display.BitmapDisplayerImpl;
import cn.com.incito.wisdom.sdk.image.display.DisplayAnim;
import cn.com.incito.wisdom.sdk.image.display.DisplayShape;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRULimitedMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRUMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.QueueProcessingType;
import cn.com.incito.wisdom.sdk.image.loader.decoder.BaseImageDecoder;
import cn.com.incito.wisdom.sdk.image.loader.decoder.ImageDecoder;
import cn.com.incito.wisdom.sdk.net.download.BaseImageDownloader;
import cn.com.incito.wisdom.sdk.net.download.ImageDownloader;
import cn.com.incito.wisdom.sdk.utils.StorageUtils;

/**
 * Factory for providing of default options for {@linkplain ImageLoaderConfiguration configuration}
 * 
 */
public class DefaultConfigurationFactory {

	/** Creates default implementation of task executor */
	public static Executor createExecutor(int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType) {
		boolean lifo = tasksProcessingType == QueueProcessingType.LIFO;
		BlockingQueue<Runnable> taskQueue = lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new LinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue, createThreadFactory(threadPriority));
	}
	/** Create default implementation of {@link DisckCacheAware} depends on incoming parameters */
	public static AbstractDiscCache createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount) {
		if (discCacheSize > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new TotalSizeLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheSize);
		} else if (discCacheFileCount > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new FileCountLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheFileCount);
		} else {
			File cacheDir = StorageUtils.getCacheDirectory(context);
			return new UnlimitedDiscCache(cacheDir, discCacheFileNameGenerator);
		}
	}

	/** Creates reserve disc cache which will be used if primary disc cache becomes unavailable */
	public static AbstractDiscCache createReserveDiscCache(Context context) {
		File cacheDir = context.getCacheDir();
		File individualDir = new File(cacheDir, "uil-images");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return new TotalSizeLimitedDiscCache(cacheDir, 2 * 1024 * 1024); // limit - 2 Mb
	}

	/**
	 * Creates default implementation of {@link MemoryCacheAware} depends on incoming parameters: <br />
	 * {@link LruMemoryCache} (for API >= 9) or {@link LRULimitedMemoryCache} (for API < 9).<br />
	 * Default cache size = 1/8 of available app memory.
	 */
	public static AbstractMemoryCache<String, Bitmap> createMemoryCache(int memoryCacheSize) {
	    if (memoryCacheSize == 0) {
	        memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
	    }
	    AbstractMemoryCache<String, Bitmap> memoryCache;
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	        memoryCache = new LRUMemoryCacheBitmapCache(memoryCacheSize);
	    } else {
	        memoryCache = new LRULimitedMemoryCacheBitmapCache(memoryCacheSize);
	    }
	    return memoryCache;
	}
	/** Creates default implementation of {@link ImageDownloader} - {@link BaseImageDownloader} */
	public static ImageDownloader createImageDownloader(Context context) {
		return new BaseImageDownloader(context);
	}

	public static ImageDecoder createImageDecoder(boolean loggingEnabled) {
	    return new BaseImageDecoder(loggingEnabled);
	}
	/** Creates default implementation of {@link BitmapDisplayer} */
	public static BitmapDisplayer createBitmapDisplayer() {
		return new BitmapDisplayerImpl(DisplayShape.DEFAULT, DisplayAnim.NONE);
	}

	/** Creates default implementation of {@linkplain ThreadFactory thread factory} for task executor */
	private static ThreadFactory createThreadFactory(int threadPriority) {
		return new DefaultThreadFactory(threadPriority);
	}

	private static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final int threadPriority;

		DefaultThreadFactory(int threadPriority) {
			this.threadPriority = threadPriority;
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}
	}
}
