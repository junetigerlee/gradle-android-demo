package cn.com.incito.wisdom.sdk.image.loader.assist;

import android.graphics.Bitmap;
import cn.com.incito.wisdom.sdk.cache.mem.model.LRULimitedMemoryCache;

public class LRULimitedMemoryCacheBitmapCache extends LRULimitedMemoryCache<String, Bitmap> {

	public LRULimitedMemoryCacheBitmapCache(int sizeLimit) {
		super(sizeLimit);
	}


	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
}
