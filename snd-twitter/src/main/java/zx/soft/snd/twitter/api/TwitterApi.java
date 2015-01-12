package zx.soft.snd.twitter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterApi {
	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private Twitter twitter;

	public TwitterApi(Twitter twitter) {
		this.twitter = twitter;
	}

	//通过用户id获取用户的基本信息
	public User showUserById(long userId) {
		User user = null;
		try {
			user = twitter.showUser(userId);
		} catch (TwitterException e) {
			logger.error("Message:{},StackTrack:{}", e.getMessage(), e.getStackTrace());
		}
		return user;
	}

	//通过用户ScreenName获取用户的基本信息
	public User showUserById(String screenName) {
		User user = null;
		try {
			user = twitter.showUser(screenName);
		} catch (TwitterException e) {
			logger.error("Message:{},StackTrack:{}", e.getMessage(), e.getStackTrace());
		}
		return user;
	}

	//通过userId获得指定授权用户的最近推文(20条)
	public ResponseList<Status> statusesUserTimelineById(long userId) throws TwitterException {
		ResponseList<Status> statuses;
		statuses = twitter.getUserTimeline(userId);
		logger.info("Showing @" + userId + "'s user timeline.");
		for (Status status : statuses) {
			logger.info("@" + status.getUser().getScreenName() + " - " + status.getText());
		}
		return statuses;
	}

	//通过userId获得指定授权用户的最近推文(20条),其中paging支持since_id, max_id, count and page 4种parameters
	public ResponseList<Status> statusesUserTimelineById(long userId, Paging paging) throws TwitterException {
		ResponseList<Status> statuses;
		statuses = twitter.getUserTimeline(userId, paging);
		logger.info("Showing @" + userId + "'s user timeline.");
		for (Status status : statuses) {
			logger.info("@" + status.getUser().getScreenName() + " - " + status.getText());
		}
		return statuses;
	}

	//通过ScreenName获得指定授权用户的最近推文(默认20条)
	public ResponseList<Status> statusesUserTimelineByScreenName(String screenName) throws TwitterException {
		ResponseList<Status> statuses;
		statuses = twitter.getUserTimeline(screenName);
		logger.info("Showing @" + screenName + "'s user timeline.");
		for (Status status : statuses) {
			logger.info("@" + status.getUser().getId() + " - " + status.getText());
			status.getRetweetedStatus();
		}
		return statuses;
	}

	//通过ScreenName获得指定授权用户的最近推文,其中paging支持since_id, max_id, count and page 4种parameters
	public ResponseList<Status> statusesUserTimelineByScreenName(String screenName, Paging paging)
			throws TwitterException {
		ResponseList<Status> statuses;
		statuses = twitter.getUserTimeline(screenName, paging);
		logger.info("Showing @" + screenName + "'s user timeline.");
		for (Status status : statuses) {
			logger.info("@" + status.getUser().getId() + " - " + status.getText());
			status.getRetweetedStatus();
		}
		return statuses;
	}

}
