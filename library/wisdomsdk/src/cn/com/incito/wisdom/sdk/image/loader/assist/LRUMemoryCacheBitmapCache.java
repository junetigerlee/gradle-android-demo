package cn.com.incito.wisdom.sdk.image.loader.assist;

import android.graphics.Bitmap;
import cn.com.incito.wisdom.sdk.cache.mem.model.LRUMemoryCache;

public class LRUMemoryCacheBitmapCache extends LRUMemoryCache<String, Bitmap> {

	public LRUMemoryCacheBitmapCache(int sizeLimit) {
		super(sizeLimit);
	}


	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
}
