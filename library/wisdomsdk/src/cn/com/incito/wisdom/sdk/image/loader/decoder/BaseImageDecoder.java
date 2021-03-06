package cn.com.incito.wisdom.sdk.image.loader.decoder;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import cn.com.incito.wisdom.sdk.image.loader.L;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageScaleType;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageSize;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageSizeUtils;
import cn.com.incito.wisdom.sdk.net.download.ImageDownloader.Scheme;
import cn.com.incito.wisdom.sdk.utils.FileUtils;

/**
 * Decodes images to {@link Bitmap}, scales them to needed size
 * 
 */
public class BaseImageDecoder implements ImageDecoder {

	protected static final String LOG_SABSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
	protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
	protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d\u00B0 [%2$s]";
	protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
	protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";

	protected boolean loggingEnabled;

	public BaseImageDecoder() {
	}

	public BaseImageDecoder(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	/**
	 * Decodes image from URI into {@link Bitmap}. Image is scaled close to
	 * incoming {@linkplain ImageSize target size} during decoding (depend on
	 * incoming parameters).
	 * 
	 * @param decodingInfo
	 *            Needed data for decoding image
	 * 
	 * @return Decoded bitmap
	 * @throws IOException
	 *             if some I/O exception occurs during image reading
	 * @throws UnsupportedOperationException
	 *             if image URI has unsupported scheme(protocol)
	 */
	public Bitmap decode(ImageDecodingInfo decodingInfo) throws IOException {
		InputStream imageStream = getImageStream(decodingInfo);
		ImageFileInfo imageInfo = defineImageSizeAndRotation(imageStream,
				decodingInfo.getImageUri());
		Options decodingOptions = prepareDecodingOptions(imageInfo.imageSize,
				decodingInfo);
		imageStream = getImageStream(decodingInfo);
		Bitmap decodedBitmap = decodeStream(imageStream, decodingOptions);
		if (decodedBitmap == null) {
			L.e(ERROR_CANT_DECODE_IMAGE, decodingInfo.getImageKey());
		} else {
			decodedBitmap = considerExactScaleAndOrientaiton(decodedBitmap,
					decodingInfo, imageInfo.exif.rotation,
					imageInfo.exif.flipHorizontal);
		}
		return decodedBitmap;
	}

	protected InputStream getImageStream(ImageDecodingInfo decodingInfo)
			throws IOException {
		return decodingInfo.getDownloader().getStream(
				decodingInfo.getImageUri(),
				decodingInfo.getExtraForDownloader());
	}

	protected ImageFileInfo defineImageSizeAndRotation(InputStream imageStream,
			String imageUri) throws IOException {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(imageStream, null, options);
		} finally {
			FileUtils.closeSilently(imageStream);
		}

		ExifInfo exif;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			exif = defineExifOrientation(imageUri, options.outMimeType);
		} else {
			exif = new ExifInfo();
		}
		return new ImageFileInfo(new ImageSize(options.outWidth,
				options.outHeight, exif.rotation), exif);
	}

	protected ExifInfo defineExifOrientation(String imageUri, String mimeType) {
		int rotation = 0;
		boolean flip = false;
		if ("image/jpeg".equalsIgnoreCase(mimeType)
				&& Scheme.ofUri(imageUri) == Scheme.FILE) {
			try {
				ExifInterface exif = new ExifInterface(
						Scheme.FILE.crop(imageUri));
				int exifOrientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
				switch (exifOrientation) {
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
					flip = true;
				case ExifInterface.ORIENTATION_NORMAL:
					rotation = 0;
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotation = 90;
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotation = 180;
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotation = 270;
					break;
				}
			} catch (IOException e) {
				L.w("Can't read EXIF tags from file [%s]", imageUri);
			}
		}
		return new ExifInfo(rotation, flip);
	}

	protected Options prepareDecodingOptions(ImageSize imageSize,
			ImageDecodingInfo decodingInfo) {
		ImageScaleType scaleType = decodingInfo.getImageScaleType();
		ImageSize targetSize = decodingInfo.getTargetSize();
		int scale = 1;
		if (scaleType != ImageScaleType.NONE) {
			boolean powerOf2 = scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2;
			scale = ImageSizeUtils.computeImageSampleSize(imageSize,
					targetSize, decodingInfo.getViewScaleType(), powerOf2);

			if (loggingEnabled)
				L.i(LOG_SABSAMPLE_IMAGE, imageSize, imageSize.scaleDown(scale),
						scale, decodingInfo.getImageKey());
		}
		Options decodingOptions = decodingInfo.getDecodingOptions();
		decodingOptions.inSampleSize = scale;
		return decodingOptions;
	}

	protected Bitmap decodeStream(InputStream imageStream,
			Options decodingOptions) throws IOException {
		try {
			return BitmapFactory.decodeStream(imageStream, null,
					decodingOptions);
		} finally {
			FileUtils.closeSilently(imageStream);
		}
	}

	protected Bitmap considerExactScaleAndOrientaiton(Bitmap subsampledBitmap,
			ImageDecodingInfo decodingInfo, int rotation, boolean flipHorizontal) {
		Matrix m = new Matrix();
		// Scale to exact size if need
		ImageScaleType scaleType = decodingInfo.getImageScaleType();
		if (scaleType == ImageScaleType.EXACTLY
				|| scaleType == ImageScaleType.EXACTLY_STRETCHED) {
			ImageSize srcSize = new ImageSize(subsampledBitmap.getWidth(),
					subsampledBitmap.getHeight(), rotation);
			float scale = ImageSizeUtils.computeImageScale(srcSize,
					decodingInfo.getTargetSize(),
					decodingInfo.getViewScaleType(),
					scaleType == ImageScaleType.EXACTLY_STRETCHED);
			if (Float.compare(scale, 1f) != 0) {
				m.setScale(scale, scale);

				if (loggingEnabled)
					L.i(LOG_SCALE_IMAGE, srcSize, srcSize.scale(scale), scale,
							decodingInfo.getImageKey());
			}
		}
		// Flip bitmap if need
		if (flipHorizontal) {
			m.postScale(-1, 1);

			if (loggingEnabled)
				L.i(LOG_FLIP_IMAGE, decodingInfo.getImageKey());
		}
		// Rotate bitmap if need
		if (rotation != 0) {
			m.postRotate(rotation);

			if (loggingEnabled)
				L.i(LOG_ROTATE_IMAGE, rotation, decodingInfo.getImageKey());
		}

		Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0,
				subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m,
				true);
		if (finalBitmap != subsampledBitmap) {
			subsampledBitmap.recycle();
		}
		return finalBitmap;
	}

	public void setLoggingEnabled(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	protected static class ExifInfo {

		protected final int rotation;
		protected final boolean flipHorizontal;

		protected ExifInfo() {
			this.rotation = 0;
			this.flipHorizontal = false;
		}

		protected ExifInfo(int rotation, boolean flipHorizontal) {
			this.rotation = rotation;
			this.flipHorizontal = flipHorizontal;
		}
	}

	protected static class ImageFileInfo {

		protected final ImageSize imageSize;
		protected final ExifInfo exif;

		protected ImageFileInfo(ImageSize imageSize, ExifInfo exif) {
			this.imageSize = imageSize;
			this.exif = exif;
		}
	}
}