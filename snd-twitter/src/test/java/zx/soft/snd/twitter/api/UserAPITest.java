package zx.soft.snd.twitter.api;

import org.junit.Test;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import zx.soft.utils.json.JsonUtils;

public class UserAPITest {

	@Test
	public void testshowUser() throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("I57Q98TSGB7Lay64XMIFtjbp4", "X8lMgSSqf9Q2bKjMb9cn3FyImnow1bXzLCVqyyHe0W1UAnajvG");
		AccessToken accessToken = new AccessToken("2922413937-5w4Ss2sg272Kje8z6G3RdDdRLxZ1oB1JQv2AWPm",
				"KcFj1fJ9ZccUwRrUgaqam0SwWSqKbxRdqBDmVhwM4iCtY");
		twitter.setOAuthAccessToken(accessToken);
		User user = twitter.showUser(2490910453L);
		user.getCreatedAt();
		System.out.println(JsonUtils.toJson(user));
		//userApi.statusesUserTimelineById(2490910453L);
	}
}
