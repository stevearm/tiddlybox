package com.horsefire.tiddly.tiddlybox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserPreferences {

	public static final String SESSION_PARAM = "userPrefs";

	public static UserPreferences get(HttpServletRequest req) {
		final HttpSession session = req.getSession();
		Object temp = session.getAttribute(SESSION_PARAM);
		if (temp instanceof UserPreferences) {
			return (UserPreferences) temp;
		}
		UserPreferences prefs = new UserPreferences();
		session.setAttribute(SESSION_PARAM, prefs);
		return prefs;
	}

	private static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	private static void assertNotEmpty(String string, String title) {
		if (isEmpty(string)) {
			throw new IllegalArgumentException(title + " can't be null");
		}
	}

	private String m_wikiPath;
	private String m_oauthTokenKey;
	private String m_oauthTokenSecret;

	public boolean needsAuthorization() {
		return isEmpty(m_oauthTokenKey) || isEmpty(m_oauthTokenSecret);
	}

	public String getWikiPath() {
		return m_wikiPath;
	}

	public String getFullWikiPath() {
		if (isEmpty(m_wikiPath)) {
			return BootstrapListener.WIKI_URL;
		}
		if (m_wikiPath.charAt(0) == '/') {
			return BootstrapListener.WIKI_URL + m_wikiPath.substring(1);
		}
		return BootstrapListener.WIKI_URL + m_wikiPath;
	}

	public void setWikiPath(String wikiPath) {
		assertNotEmpty(wikiPath, "Path");
		if (!wikiPath.endsWith(".html") && !wikiPath.endsWith(".htm")) {
			throw new IllegalArgumentException(
					"Path must be an htm or html file");
		}
		m_wikiPath = wikiPath;
	}

	public String getOauthTokenKey() {
		return m_oauthTokenKey;
	}

	public void setOauthTokenKey(String oauthTokenKey) {
		assertNotEmpty(oauthTokenKey, "Key");
		m_oauthTokenKey = oauthTokenKey;
	}

	public String getOauthTokenSecret() {
		return m_oauthTokenSecret;
	}

	public void setOauthTokenSecret(String oauthTokenSecret) {
		assertNotEmpty(oauthTokenSecret, "Secret");
		m_oauthTokenSecret = oauthTokenSecret;
	}
}
