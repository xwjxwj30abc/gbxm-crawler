package zx.soft.snd.twitter.api;

import java.util.List;

import org.junit.Test;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.domain.StatusInfo;

public class TwitterApiTest {

	//	@Ignore
	@Test
	public void teststatusesHomeTimeline() throws InterruptedException, TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("bLYOx0aH6uXoq1zPV14ePduBC", "RKcpOUKPyBZy3lJcOhFWIztGJnK9LsGugWPU7y7ekpYvaFbQCs");
		AccessToken accessToken1 = new AccessToken("2922413937-khq0Hh3Otv6AItaFNCtnWRVWWRjVnT4BoBjIxoI",
				"q4nWHrNsAMcwIjwiuQbJnpKpBo11fFk0lTg3zS1mQWFpe");
		AccessToken accessToken2 = new AccessToken("2879495431-gxnoteJes2wH3UbfGImDrGS9C2xfFAQEeRxgk6E",
				"6kwmsLvARCGGkvYFqY7noGbeo6gv2CruUoNQYt0ymjQ99");
		twitter.setOAuthAccessToken(accessToken2);
		TwitterApi twitterApi = new TwitterApi(twitter);
		while (true) {
			List<StatusInfo> statusInfoes = twitterApi.statusesHomeTimeline();
			System.out.println("statusInfoes \'s size=" + statusInfoes.size());
			Thread.currentThread().sleep(30000);
		}
	}

}
