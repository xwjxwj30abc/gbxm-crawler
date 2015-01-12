package zx.soft.snd.twitter.test;

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
		AccessToken accessToken = new AccessToken("2960145383-twSYPbJwZ3uGibd014Qkd1TWBguMl4zutGrPvw9",
				"l2RMi0w6Rw67mnXMJZqVJ6qxTzv4cW2qLajkMd136F8FP");
		//		token : 2490910454-idLUwxLp96F1GRWhDh1uKelEdYbL9lr80HbtVoY
		//		tokenSecret : KOf18jCAfes8VIkNmekzI2pAh8pbbUQ5cpfZ3bCNweiVd
		//@xwjfznx  token : 2922413937-5w4Ss2sg272Kje8z6G3RdDdRLxZ1oB1JQv2AWPm
		//tokenSecret : KcFj1fJ9ZccUwRrUgaqam0SwWSqKbxRdqBDmVhwM4iCtY
		twitter.setOAuthAccessToken(accessToken);
		User user = twitter.showUser("xwj_xwj");
		System.out.println(JsonUtils.toJson(user));
	}

}
