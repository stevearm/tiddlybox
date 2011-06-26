package com.horsefire.tiddly.tiddlybox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropboxWikiClient {

	public static final String VALUE_REQUEST_TOKEN_URL = "http://api.getdropbox.com/0/oauth/request_token";
	public static final String VALUE_ACCESS_TOKEN_URL = "http://api.getdropbox.com/0/oauth/access_token";
	public static final String VALUE_AUTHORIZATION_URL = "http://api.getdropbox.com/0/oauth/authorize";
	public static final String FILE_BASE_URL = "https://api-content.dropbox.com/0/files/dropbox";

	private static final Logger LOG = LoggerFactory
			.getLogger(DropboxWikiClient.class);

	private final OAuthConsumer m_consumer;
	private final String m_wikiPath;
	private final String m_wikiFileName;

	public DropboxWikiClient(String oauthKey, String oauthSecret,
			String wikiPath) {
		m_consumer = new CommonsHttpOAuthConsumer(
				AppCredentials.INSTANCE.getKey(),
				AppCredentials.INSTANCE.getSecret());
		m_consumer.setTokenWithSecret(oauthKey, oauthSecret);

		int index = wikiPath.lastIndexOf("/");
		if (index == -1) {
			throw new IllegalArgumentException("Path must have a / in it");
		}
		m_wikiPath = wikiPath.substring(0, index);
		m_wikiFileName = wikiPath.substring(index + 1);
	}

	public void pushWiki(String wiki) throws IOException {
		LOG.debug("Pushing contents to {}/{}", m_wikiPath, m_wikiFileName);
		HttpPost req = new HttpPost(DropboxWikiClient.FILE_BASE_URL
				+ m_wikiPath);

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("file", m_wikiFileName));
		req.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		try {
			m_consumer.sign(req);
		} catch (OAuthMessageSignerException e) {
			throw new IOException(e);
		} catch (OAuthExpectationFailedException e) {
			throw new IOException(e);
		} catch (OAuthCommunicationException e) {
			throw new IOException(e);
		}

		// now we can add the real file multipart and we're good
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("file", new StringBody(wiki) {
			@Override
			public String getFilename() {
				return m_wikiFileName;
			}
		});
		req.setEntity(entity);

		DefaultHttpClient client = new DefaultHttpClient();

		client.execute(req, new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				HttpEntity ent = response.getEntity();
				if (LOG.isDebugEnabled()) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ent.writeTo(baos);
					StatusLine statusLine = response.getStatusLine();
					LOG.debug("Response: {} {} {}",
							new Object[] { statusLine.getStatusCode(),
									statusLine.getReasonPhrase(),
									new String(baos.toByteArray()) });
				}
				ent.consumeContent();
				return "";
			}
		});
	}
}
