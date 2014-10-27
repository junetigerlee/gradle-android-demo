package cn.com.incito.wisdom.sdk.image.loader.assist;

import android.graphics.Bitmap;
import cn.com.incito.wisdom.sdk.cache.mem.model.UsingFreqLimitedMemoryCache;

public class UsingFreqLimitedMemoryBitmapCache extends UsingFreqLimitedMemoryCache<String, Bitmap> {

	public UsingFreqLimitedMemoryBitmapCache(int sizeLimit) {
		super(sizeLimit);
	}


	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
}
