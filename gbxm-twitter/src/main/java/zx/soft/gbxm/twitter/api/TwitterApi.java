package zx.soft.gbxm.twitter.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import zx.soft.utils.config.ConfigUtil;

public class TwitterApi {
	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private Twitter twitter;
	private static final int COUNT = 200;//每页的数量,取最大值200
	private static int PAGE = 1;//获取第一页信息
	private static long SINCEID = 1L;
	private static long MAXID = 2000000000000000001L;

	public TwitterApi() {
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
	}

	/**
	 * 获取twitter用户的主页动态信息
	 * @return
	 * @throws TwitterException
	 * @throws InterruptedException
	 */
	public List<Status> getHomeTimeLine() throws TwitterException {
		boolean flag = true;
		long lastTimeSinceId = SINCEID;
		List<Status> statuses = new ArrayList<>();
		while (flag) {
			Paging paging = new Paging(PAGE, COUNT, lastTimeSinceId, MAXID);
			ResponseList<Status> tempStatuses = twitter.getHomeTimeline(paging);
			PAGE++;
			if (tempStatuses.size() == 0) {
				flag = false;
				PAGE = 1;
			}
			statuses.addAll(tempStatuses);
			MAXID = statuses.get(0).getId();
			tempStatuses = null;
		}
		SINCEID = statuses.get(0).getId();
		logger.info("update sinceId=" + SINCEID);
		return statuses;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
