package cn.com.incito.wisdom.sdk.image.loader.decoder;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import cn.com.incito.wisdom.sdk.image.loader.DisplayImageOptions;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageScaleType;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageSize;
import cn.com.incito.wisdom.sdk.image.loader.assist.MemoryCacheUtil;
import cn.com.incito.wisdom.sdk.image.loader.assist.ViewScaleType;
import cn.com.incito.wisdom.sdk.net.download.ImageDownloader;

/**
 * Contains needed information for decoding image to Bitmap
 *
 */
public class ImageDecodingInfo {

    private final String imageKey;
    private final String imageUri;
    private final ImageSize targetSize;

    private final ImageScaleType imageScaleType;
    private final ViewScaleType viewScaleType;

    private final ImageDownloader downloader;
    private final Object extraForDownloader;

    private final Options decodingOptions;

    public ImageDecodingInfo(String imageKey, String imageUri, ImageSize targetSize, ViewScaleType viewScaleType, ImageDownloader downloader, DisplayImageOptions displayOptions) {
        this.imageKey = imageKey;
        this.imageUri = imageUri;
        this.targetSize = targetSize;

        this.imageScaleType = displayOptions.getImageScaleType();
        this.viewScaleType = viewScaleType;

        this.downloader = downloader;
        this.extraForDownloader = displayOptions.getExtraForDownloader();

        decodingOptions = new Options();
        copyOptions(displayOptions.getDecodingOptions(), decodingOptions);
    }

    private void copyOptions(Options srcOptions, Options destOptions) {
        destOptions.inDensity = srcOptions.inDensity;
        destOptions.inDither = srcOptions.inDither;
        destOptions.inInputShareable = srcOptions.inInputShareable;
        destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
        destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
        destOptions.inPurgeable = srcOptions.inPurgeable;
        destOptions.inSampleSize = srcOptions.inSampleSize;
        destOptions.inScaled = srcOptions.inScaled;
        destOptions.inScreenDensity = srcOptions.inScreenDensity;
        destOptions.inTargetDensity = srcOptions.inTargetDensity;
        destOptions.inTempStorage = srcOptions.inTempStorage;
        if (Build.VERSION.SDK_INT >= 10) copyOptions10(srcOptions, destOptions);
        if (Build.VERSION.SDK_INT >= 11) copyOptions11(srcOptions, destOptions);
    }

    @TargetApi(10)
    private void copyOptions10(Options srcOptions, Options destOptions) {
        destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
    }

    @TargetApi(11)
    private void copyOptions11(Options srcOptions, Options destOptions) {
        destOptions.inBitmap = srcOptions.inBitmap;
        destOptions.inMutable = srcOptions.inMutable;
    }

    /**
     * @return Original {@linkplain MemoryCacheUtil#generateKey(String, ImageSize) image key} (used in memory cache).
     */
    public String getImageKey() {
        return imageKey;
    }

    /**
     * @return Image URI for decoding (usually image from disc cache)
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * @return Target size for image. Decoded bitmap should close to this size according to {@linkplain ImageScaleType
     *         image scale type} and {@linkplain ViewScaleType view scale type}.
     */
    public ImageSize getTargetSize() {
        return targetSize;
    }

    /**
     * @return {@linkplain ImageScaleType Scale type for image sampling and scaling}. This parameter affects result size
     *         of decoded bitmap.
     */
    public ImageScaleType getImageScaleType() {
        return imageScaleType;
    }

    /**
     * @return {@linkplain ViewScaleType View scale type}. This parameter affects result size of decoded bitmap.
     */
    public ViewScaleType getViewScaleType() {
        return viewScaleType;
    }

    /**
     * @return Downloader for image loading
     */
    public ImageDownloader getDownloader() {
        return downloader;
    }

    /**
     * @return Auxiliary object for downloader
     */
    public Object getExtraForDownloader() {
        return extraForDownloader;
    }

    /**
     * @return Decoding options
     */
    public Options getDecodingOptions() {
        return decodingOptions;
    }
}