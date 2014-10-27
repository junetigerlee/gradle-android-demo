package cn.com.incito.wisdom.sdk.net.download;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decorator. Prevents downloads from network (throws {@link IllegalStateException exception}).<br />
 * In most cases this downloader shouldn't be used directly.
 *
 */
public class NetworkDeniedImageDownloader implements ImageDownloader {

	private final ImageDownloader wrappedDownloader;

	public NetworkDeniedImageDownloader(ImageDownloader wrappedDownloader) {
		this.wrappedDownloader = wrappedDownloader;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				throw new IllegalStateException();
			default:
				return wrappedDownloader.getStream(imageUri, extra);
		}
	}
}
