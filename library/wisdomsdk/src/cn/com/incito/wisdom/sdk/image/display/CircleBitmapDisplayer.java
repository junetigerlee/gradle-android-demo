package cn.com.incito.wisdom.sdk.image.display;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Displays bitmap with rounded corners. <br />
 * <b>NOTE:</b> New {@link Bitmap} object is created for displaying. So this class needs more memory and can cause
 * {@link OutOfMemoryError}.
 * 
 */
public class CircleBitmapDisplayer{


	private CircleBitmapDisplayer() {
	}
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dstLeft, dstTop, dstRight, dstBottom;
        int size = width > height ? height : width;
        roundPx = size / 2;
        top = 0;
        bottom = size;
        left = 0;
        right = size;
        height = size;
        dstLeft = 0;
        dstTop = 0;
        dstRight = size;
        dstBottom = size;

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dstLeft, (int) dstTop, (int) dstRight,
                (int) dstBottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

}
