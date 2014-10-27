package cn.com.incito.wisdom.sdk.image.display;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Displays bitmap with flip. <br />
 * https://github.com/castorflex/FlipImageView <b>NOTE:</b> New {@link Bitmap}
 * object is created for displaying. So this class needs more memory and can
 * cause {@link OutOfMemoryError}.
 * 
 */
public class FlipBitmapDisplayer {
	public static final int FLAG_ROTATION_X = 1 << 0;

	public static final int FLAG_ROTATION_Y = 1 << 1;

	public static final int FLAG_ROTATION_Z = 1 << 2;

	private static boolean mIsRotationXEnabled;

	private static boolean mIsRotationYEnabled;

	private static boolean mIsRotationZEnabled;

	private static Interpolator mInterpolator;

	private static int mFlipDuration;

	private static FlipBitmapDisplayer flipBitmapDisplayer = new FlipBitmapDisplayer();
	private FlipBitmapDisplayer() {

	}

	public static void setFlipData(int flipRotations,
			Interpolator interpolator, int duration) {
		mIsRotationXEnabled = (flipRotations & FLAG_ROTATION_X) != 0;
		mIsRotationYEnabled = (flipRotations & FLAG_ROTATION_Y) != 0;
		mIsRotationZEnabled = (flipRotations & FLAG_ROTATION_Z) != 0;
		mInterpolator = interpolator;
		mFlipDuration = duration;
		if (mInterpolator == null) {
			mInterpolator = new DecelerateInterpolator();
		}
	}

	public static void startAnimation(Bitmap bitmap, ImageView imageView, boolean isApplyAnim) {
		if (isApplyAnim == true) {

			FlipAnimator animation = flipBitmapDisplayer.new FlipAnimator(
					bitmap, imageView);
			animation.setInterpolator(mInterpolator);
			animation.setDuration(mFlipDuration);
			imageView.startAnimation(animation);

		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * Animation part All credits goes to coomar
	 */
	public class FlipAnimator extends Animation {

		private Camera camera;

		private Bitmap bitmap;

		private ImageView imageView;

		private float centerX;

		private float centerY;

		private boolean visibilitySwapped;

		public FlipAnimator(Bitmap to, ImageView iv) {
			bitmap = to;
			imageView = iv;
			visibilitySwapped = false;
		}

		public FlipAnimator() {
			setFillAfter(true);
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			camera = new Camera();
			this.centerX = width / 2;
			this.centerY = height / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			// Angle around the y-axis of the rotation at the given time. It is
			// calculated both in radians and in the equivalent degrees.
			final double radians = Math.PI * interpolatedTime;
			float degrees = (float) (180.0 * radians / Math.PI);

			// Once we reach the midpoint in the animation, we need to hide the
			// source view and show the destination view. We also need to change
			// the angle by 180 degrees so that the destination does not come in
			// flipped around. This is the main problem with SDK sample, it does
			// not
			// do this.
			if (interpolatedTime >= 0.5f) {
				degrees -= 180.f;
				if (!visibilitySwapped) {
					imageView.setImageBitmap(bitmap);
					visibilitySwapped = true;
				}
			}

			final Matrix matrix = t.getMatrix();

			camera.save();
			camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
			camera.rotateX(mIsRotationXEnabled ? degrees : 0);
			camera.rotateY(mIsRotationYEnabled ? degrees : 0);
			camera.rotateZ(mIsRotationZEnabled ? degrees : 0);
			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}

	}

}
