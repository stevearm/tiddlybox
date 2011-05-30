package com.horsefire.tiddly.tiddlybox;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class WikiServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(WikiServlet.class);

	private static String getUpdatedWikiContents(UserPreferences prefs)
			throws IOException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		final OAuthConsumer consumer = new DefaultOAuthConsumer(
				DropboxWikiClient.VALUE_CONSUMER_KEY,
				DropboxWikiClient.VALUE_CONSUMER_SECRET);
		consumer.setTokenWithSecret(prefs.getOauthTokenKey(),
				prefs.getOauthTokenSecret());

		final URL url = new URL(DropboxWikiClient.FILE_BASE_URL
				+ prefs.getWikiPath());
		final HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		consumer.sign(connection);
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String contents = Util.extractString(connection.getInputStream());
			return contents;
		}
		LOG.error("Error getting wiki. Got response {}",
				connection.getResponseCode());
		throw new IOException();
	}

	private final WikiMorpher m_morpher;

	@Inject
	public WikiServlet(WikiMorpher morpher) {
		m_morpher = morpher;
	}

	private UserPreferences getPrefs(HttpServletRequest req, String path) {
		UserPreferences prefs = UserPreferences.get(req);
		prefs.setWikiPath(path);
		return prefs;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		final UserPreferences prefs = getPrefs(req, req.getPathInfo());
		if (prefs.needsAuthorization()) {
			resp.sendRedirect(BootstrapListener.HANDSHAKE_ONE_URL);
			return;
		}

		try {
			final String contents = getUpdatedWikiContents(prefs);
			PrintWriter out = resp.getWriter();
			out.println(m_morpher.prepareToServe(contents));
		} catch (OAuthMessageSignerException e) {
			error(resp, e);
		} catch (OAuthExpectationFailedException e) {
			error(resp, e);
		} catch (OAuthCommunicationException e) {
			error(resp, e);
		}
	}

	private void error(HttpServletResponse resp, Exception e)
			throws IOException {
		String message = "Problem displaying wiki";
		resp.getWriter().print(message);
		LOG.error(message, e);
	}

	private void pushWikiContents(UserPreferences prefs, String content)
			throws IOException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		final DropboxWikiClient client = new DropboxWikiClient(
				prefs.getOauthTokenKey(), prefs.getOauthTokenSecret(),
				prefs.getWikiPath());
		client.pushWiki(content);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final PrintWriter out = resp.getWriter();
		final UserPreferences prefs = getPrefs(req, req.getPathInfo());
		if (prefs.needsAuthorization()) {
			out.println("{\"status\":1}");
			return;
		}

		final String store = Util.extractString(req.getInputStream());

		try {
			String newFile = m_morpher.prepareToSave(
					getUpdatedWikiContents(prefs), store);
			out.println(newFile);
			pushWikiContents(prefs, newFile);
		} catch (OAuthMessageSignerException e) {
			resp.getWriter().print("Failure. See logs");
			LOG.error("Problem displaying wiki", e);
		} catch (OAuthExpectationFailedException e) {
			resp.getWriter().print("Failure. See logs");
			LOG.error("Problem displaying wiki", e);
		} catch (OAuthCommunicationException e) {
			resp.getWriter().print("Failure. See logs");
			LOG.error("Problem displaying wiki", e);
		}
	}
}
