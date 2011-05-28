package com.horsefire.tiddly.tiddlybox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class HandshakeOneServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		final UserPreferences prefs = UserPreferences.get(req);

		final OAuthConsumer consumer = new DefaultOAuthConsumer(
				DropboxApi.VALUE_CONSUMER_KEY, DropboxApi.VALUE_CONSUMER_SECRET);
		final OAuthProvider provider = new DefaultOAuthProvider(
				DropboxApi.VALUE_REQUEST_TOKEN_URL,
				DropboxApi.VALUE_ACCESS_TOKEN_URL,
				DropboxApi.VALUE_AUTHORIZATION_URL);

		try {
			final String url = provider.retrieveRequestToken(consumer,
					TiddlyBoxUrls.BASE_URL
							+ BootstrapListener.HANDSHAKE_TWO_URL);
			prefs.setOauthTokenKey(consumer.getToken());
			prefs.setOauthTokenSecret(consumer.getTokenSecret());
			resp.sendRedirect(url);
		} catch (OAuthMessageSignerException e) {
			resp.getWriter().print("Failure. See logs");
			e.printStackTrace(System.out);
		} catch (OAuthNotAuthorizedException e) {
			resp.getWriter().print("Failure. See logs");
			e.printStackTrace(System.out);
		} catch (OAuthExpectationFailedException e) {
			resp.getWriter().print("Failure. See logs");
			e.printStackTrace(System.out);
		} catch (OAuthCommunicationException e) {
			resp.getWriter().print("Failure. See logs");
			e.printStackTrace(System.out);
		}
	}
}