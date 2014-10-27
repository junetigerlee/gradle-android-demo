package cn.com.incito.wisdom.sdk.image.display;

import android.graphics.Bitmap;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import cn.com.incito.wisdom.sdk.image.loader.assist.LoadedFrom;

/**
 * Just displays {@link Bitmap} in {@link ImageView}
 * 
 */


public final class BitmapDisplayerImpl implements BitmapDisplayer {
	private int defaultPixels = 90;
	private int durationMillis = 400;
	private int flipRotations = 90;
	private Interpolator interpolator;
	private DisplayShape shape;
	private DisplayAnim anim;
	public BitmapDisplayerImpl(DisplayShape shape, DisplayAnim anim){
		this.shape = shape;
		this.anim = anim;
	}

	public BitmapDisplayerImpl(DisplayShape shape, DisplayAnim anim, int durationMillis){
		this.shape = shape;
		this.anim = anim;
		this.durationMillis = durationMillis;
	}
	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView, boolean isApplyAnim, LoadedFrom loadedFrom) {
		Bitmap localBitmap = null;
		switch(shape)
		{
			case CIRCLE:
				try{
					localBitmap = CircleBitmapDisplayer.getCircleBitmap(bitmap);
				}catch(OutOfMemoryError e){
					localBitmap = bitmap;
//					WLog.e(CircleBitmapDisplayer.class, "Can't create bitmap with rounded corners. Not enough memory.", e);
				}
				break;
			case ROUND:
				try{
					localBitmap = RoundedBitmapDisplayer.roundCorners(bitmap, imageView, defaultPixels);
				}catch(OutOfMemoryError e){
					localBitmap = bitmap;
//					WLog.e(CircleBitmapDisplayer.class, "Can't create bitmap with rounded corners. Not enough memory.", e);
				}
				break;
			default:
				localBitmap = bitmap;
				break;
		}
		switch(anim)
		{
			case FADE_IN:
				imageView.setImageBitmap(localBitmap);
				FadeInBitmapDisplayer.animate(imageView, durationMillis);
				break;
			case FLIP:
				imageView.setImageBitmap(localBitmap);
				FlipBitmapDisplayer.setFlipData(flipRotations, interpolator, durationMillis);
				FlipBitmapDisplayer.startAnimation(localBitmap, imageView, isApplyAnim);
				break;
			default:
				imageView.setImageBitmap(localBitmap);
			break;
		}
		return localBitmap;
	}
	public void setflipRotations(int flipRotations){
		this.flipRotations = flipRotations;
	}
	public void setDuration(int durationMillis){
		this.durationMillis = durationMillis;
	}
	public void setRoundPixels(int defaultPixels)
	{
		this.defaultPixels = defaultPixels;
	}
	public void setInterpolator(Interpolator interpolator)
	{
		this.interpolator = interpolator;
	}
}