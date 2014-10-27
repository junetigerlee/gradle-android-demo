package cn.com.incito.wisdom.sdk.image.display;

import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Displays image with "fade in" animation
 */
public class FadeInBitmapDisplayer {

	private FadeInBitmapDisplayer() {

	}
	/**
	 * Animates {@link ImageView} with "fade-in" effect
	 * 
	 * @param imageView {@link ImageView} which display image in
	 * @param durationMillis The length of the animation in milliseconds
	 */
	public static void animate(ImageView imageView, int durationMillis) {
		AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
		fadeImage.setDuration(durationMillis);
		fadeImage.setInterpolator(new DecelerateInterpolator());
		imageView.startAnimation(fadeImage);
	}

}
