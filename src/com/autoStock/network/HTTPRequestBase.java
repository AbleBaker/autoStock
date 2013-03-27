package com.autoStock.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author Kevin Kowalewski
 * 
 */
public abstract class HTTPRequestBase {
	protected HttpClient httpClient = new DefaultHttpClient();
	protected HttpGet httpGet = new HttpGet();
	protected Thread thread;

	public static enum HttpFaultResponse {
		fault_status_code, fault_unknown,
	}

	public void sendHttpGetRequest(final String url) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					httpGet.setURI(new URI(url));
					HttpResponse httpResponse = httpClient.execute(httpGet);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						receivedResponse(httpResponse.getEntity());
					} else {
						receivedFaultResponse(HttpFaultResponse.fault_status_code);
					}
				} catch (Exception e) {
					e.printStackTrace();
					receivedFaultResponse(HttpFaultResponse.fault_unknown);
				}
			}
		});
		
		thread.start();
	}

	public abstract void receivedResponse(HttpEntity httpEntity);
	public abstract void receivedFaultResponse(HttpFaultResponse httpFaultResponse);
}
