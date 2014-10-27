package cn.com.incito.wisdom.sdk.image.display;

import android.graphics.Bitmap;
import android.widget.ImageView;
import cn.com.incito.wisdom.sdk.image.loader.assist.LoadedFrom;

/**
 * Displays {@link Bitmap} in {@link ImageView}. Successors can apply some changes to Bitmap before displaying.
 * 
 */
public interface BitmapDisplayer {

	/**
	 * Display bitmap in {@link ImageView}. Incoming bitmap can be changed any way before displaying. Displayed bitmap
	 * should be returned.
	 * 
	 * @param bitmap
	 *            Source bitmap
	 * @param imageView
	 *            {@link ImageView Image view}
	 * @param isApplyAnim
	 *				Apply animation when bitmap is loaded from cache
	 * @param loadedFrom 
	 *             Source of loaded image
	 * @return Bitmap which was displayed in {@link ImageView}
	 */
	Bitmap display(Bitmap bitmap, ImageView imageView, boolean isApplyAnim, LoadedFrom loadedFrom);
}
