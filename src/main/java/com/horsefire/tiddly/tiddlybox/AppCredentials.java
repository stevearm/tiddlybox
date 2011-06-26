package com.horsefire.tiddly.tiddlybox;

import org.slf4j.LoggerFactory;

public class AppCredentials {

	public static final AppCredentials INSTANCE = new AppCredentials();

	private static final String KEY_APPKEY = "appkey";
	private static final String KEY_APPSECRET = "appsecret";

	private final String m_key;
	private final String m_secret;

	public AppCredentials() {
		String key = System.getProperty(KEY_APPKEY);
		String secret = System.getProperty(KEY_APPSECRET);
		if (key != null && !key.isEmpty() && secret != null
				&& !secret.isEmpty()) {
			m_key = key;
			m_secret = secret;
		} else {
			String message = "Must define " + KEY_APPKEY + " and "
					+ KEY_APPSECRET + " environment variables";
			LoggerFactory.getLogger(AppCredentials.class).error(message);
			throw new RuntimeException(message);
		}
	}

	public String getKey() {
		return m_key;
	}

	public String getSecret() {
		return m_secret;
	}
}
