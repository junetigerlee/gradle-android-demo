package cn.com.incito.wisdom.sdk.net.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

public class SimpleSyncHttpClient extends AsyncHttpClient {

    // Private stuff
    protected void sendRequest(DefaultHttpClient client,
            HttpContext httpContext, HttpUriRequest uriRequest,
            String contentType, AsyncHttpResponseHandler responseHandler,
            Context context) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }

        /*
         * will execute the request directly
         */
        new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler)
                .run();
    }
}
