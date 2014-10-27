package cn.com.incito.wisdom.sdk.net.download;

import java.io.IOException;
import java.io.InputStream;

import cn.com.incito.wisdom.sdk.image.loader.assist.FlushedInputStream;

/**
 * Decorator. Handles <a href="http://code.google.com/p/android/issues/detail?id=6066">this problem</a> on slow networks
 * using {@link FlushedInputStream}.
 *
 */
public class SlowNetworkImageDownloader implements ImageDownloader {

	private final ImageDownloader wrappedDownloader;

	public SlowNetworkImageDownloader(ImageDownloader wrappedDownloader) {
		this.wrappedDownloader = wrappedDownloader;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		InputStream imageStream = wrappedDownloader.getStream(imageUri, extra);
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return new FlushedInputStream(imageStream);
			default:
				return imageStream;
		}
	}
}
