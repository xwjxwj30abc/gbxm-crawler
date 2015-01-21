package zx.soft.snd.twitter.api;

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
import twitter4j.User;
import zx.soft.utils.config.ConfigUtil;

public class TwitterApi {

	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private Twitter twitter;
	private static final int COUNT = 200;//每页的数量,取最大值200
	private static int PAGE = 1;//获取第一页信息
	public static long SINCEID = 1L;

	public TwitterApi() {
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
	}

	public TwitterApi(Twitter twitter) {
		this.twitter = twitter;
	}

	/**
	 * 通过用户名添加关注
	 * @param screenName
	 * @return  返回是否关注成功
	 * @throws TwitterException
	 */
	public void autoFollow(String screenName) throws TwitterException {
		User user = twitter.createFriendship(screenName);
		logger.info("auto follow " + user.getScreenName() + "successful");
	}

	/**
	 * 获取twitter用户的主页动态信息
	 * @return
	 * @throws TwitterException
	 * @throws InterruptedException
	 */
	public List<Status> getHomeTimeLine() throws TwitterException {
		Paging paging = new Paging(PAGE, COUNT, SINCEID);
		ResponseList<Status> statuses = null;
		statuses = twitter.getHomeTimeline(paging);
		logger.info("first request get status size=" + statuses.size());
		boolean flag = false;
		long lastTimeSinceId = SINCEID;
		if (statuses.size() > 0) {
			flag = true;
			SINCEID = statuses.get(0).getId();
			logger.info("set sinceId=" + SINCEID);
		}
		while (flag) {
			try {
				PAGE++;
				Paging nextPaging = new Paging(PAGE, COUNT, lastTimeSinceId);
				ResponseList<Status> nextStatuses = twitter.getHomeTimeline(nextPaging);
				logger.info("page=" + PAGE + ",size=" + nextStatuses.size());
				if (nextStatuses.size() == 0) {
					flag = false;
				} else {
					statuses.addAll(nextStatuses);
					nextStatuses = null;
				}
			} catch (TwitterException e) {
				logger.info("page too long...start sleep 15min");
				try {
					Thread.sleep(900000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				PAGE--;
			}
		}
		PAGE = 1;
		return statuses;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
