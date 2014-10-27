package cn.com.incito.wisdom.sdk.image.loader.decoder;

import java.io.IOException;

import android.graphics.Bitmap;

/**
 * Provide decoding image to result {@link Bitmap}.
 *
 */
public interface ImageDecoder {

    /**
     * Decodes image to {@link Bitmap} according target size and other parameters.
     * 
     * @param imageDecodingInfo 
     * @return
     * @throws IOException
     */
    Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException;
}