package cn.com.incito.wisdom.sdk.image.loader;

import cn.com.incito.wisdom.sdk.image.loader.assist.LoadedFrom;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Presents process'n'display image task. Processes image {@linkplain Bitmap} and display it in {@link ImageView} using
 * {@link DisplayBitmapTask}.
 *
 */
class ProcessAndDisplayImageTask implements Runnable {

    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";

	private final ImageLoaderEngine engine;
	private final Bitmap bitmap;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.engine = engine;
		this.bitmap = bitmap;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;
	}

	@Override
	public void run() {
		if (engine.configuration.loggingEnabled) L.i(LOG_POSTPROCESS_IMAGE, imageLoadingInfo.memoryCacheKey);
		BitmapProcessor processor = imageLoadingInfo.options.getPostProcessor();
		final Bitmap processedBitmap = processor.process(bitmap);
		handler.post(new DisplayBitmapTask(processedBitmap, imageLoadingInfo, engine, false, LoadedFrom.MEMORY_CACHE));
	}
}
