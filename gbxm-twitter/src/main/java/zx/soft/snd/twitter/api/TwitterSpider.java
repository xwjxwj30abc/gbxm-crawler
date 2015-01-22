package zx.soft.snd.twitter.api;

import twitter4j.auth.AccessToken;

public class TwitterSpider {

	public static void main(String[] args) throws InterruptedException {
		AccessToken accessToken = new AccessToken("2922413937-khq0Hh3Otv6AItaFNCtnWRVWWRjVnT4BoBjIxoI",
				"q4nWHrNsAMcwIjwiuQbJnpKpBo11fFk0lTg3zS1mQWFpe");
		FollowsTwitterSpider followsTwitterSpider = new FollowsTwitterSpider(accessToken);
		int[] diffLength = { 0, 0 };
		long sleepTime = 0L;
		while (true) {
			diffLength[0] = diffLength[1];
			diffLength[1] = followsTwitterSpider.run();
			if (diffLength[1] == 0) {
				sleepTime = (long) (200 - diffLength[0]) * 30000;
				Thread.sleep(sleepTime);
			}
		}
	}
}
