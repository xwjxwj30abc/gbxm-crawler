package zx.soft.snd.twitter.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class OAuthSetup {

	public static void main(String args[]) throws Exception {

		// The factory instance is re-useable and thread safe.
		Twitter twitter = new TwitterFactory().getInstance();
		//insert the appropriate consumer key and consumer secret here
		twitter.setOAuthConsumer("I57Q98TSGB7Lay64XMIFtjbp4", "X8lMgSSqf9Q2bKjMb9cn3FyImnow1bXzLCVqyyHe0W1UAnajvG");
		RequestToken requestToken = twitter.getOAuthRequestToken();
		//System.out.println("requestToken : " + requestToken.getToken());
		//System.out.println("requestTokenSecret : " + requestToken.getTokenSecret());
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out.println("Open the following URL and grant access to your account:");
			System.err.println(requestToken.getAuthorizationURL());
			System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}

		//persist to the accessToken for future reference.
		System.out.println(twitter.verifyCredentials().getId());
		System.out.println("token : " + accessToken.getToken());
		System.out.println("tokenSecret : " + accessToken.getTokenSecret());
	}
}
