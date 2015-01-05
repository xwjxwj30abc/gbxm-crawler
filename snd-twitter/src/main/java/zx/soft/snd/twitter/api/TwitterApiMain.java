package zx.soft.snd.twitter.api;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import zx.soft.utils.json.JsonUtils;

public class TwitterApiMain {

	public static void main(String[] args) throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("I57Q98TSGB7Lay64XMIFtjbp4", "X8lMgSSqf9Q2bKjMb9cn3FyImnow1bXzLCVqyyHe0W1UAnajvG");
		AccessToken accessToken = new AccessToken("2490910454-idLUwxLp96F1GRWhDh1uKelEdYbL9lr80HbtVoY",
				"KOf18jCAfes8VIkNmekzI2pAh8pbbUQ5cpfZ3bCNweiVd");
		//		token : 2490910454-idLUwxLp96F1GRWhDh1uKelEdYbL9lr80HbtVoY
		//		tokenSecret : KOf18jCAfes8VIkNmekzI2pAh8pbbUQ5cpfZ3bCNweiVd
		twitter.setOAuthAccessToken(accessToken);
		User user = twitter.showUser("rsarver");
		System.out.println(JsonUtils.toJson(user));
	}

}
