package zx.soft.snd.twitter.api;

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
import twitter4j.User;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.utils.config.ConfigUtil;

public class TwitterApi {

	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private Twitter twitter;
	private static final int PAGE = 1;//获取第一页信息
	private static final int COUNT = 200;//每页的数量,取最大值200
	private static long SINCE_ID = 1L;

	public TwitterApi() {
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		Twitter initTwitter = new TwitterFactory().getInstance();
		initTwitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
		this.twitter = initTwitter;
	}

	public TwitterApi(Twitter twitter) {
		this.twitter = twitter;
	}

	/**
	 * 通过用户名添加关注
	 * @param screenName
	 * @return  返回是否关注成功
	 */
	public boolean autoFollow(String screenName) {
		boolean successful = false;
		try {
			User user = twitter.createFriendship(screenName);
			successful = true;
			logger.info("auto follow " + user.getScreenName() + "successful");
		} catch (TwitterException e) {
			successful = false;
			logger.error("Exception:{},StackTrace:{}", e.getMessage(), e.getStackTrace());
		}
		return successful;
	}

	/**
	 * 获取twitter用户的主页动态信息
	 * @param sinceId
	 * @return 主页动态信息
	 * @throws TwitterException
	 */
	public List<StatusInfo> statusesHomeTimeline() throws TwitterException {
		List<StatusInfo> statusInfoes = new ArrayList<>();
		Paging paging = new Paging(PAGE, COUNT, SINCE_ID);
		ResponseList<Status> statuses = twitter.getHomeTimeline(paging);
		if (statuses.size() > 0) {
			for (Status status : statuses) {
				StatusInfo statusInfo = new StatusInfo();
				statusInfo.setCreatedAt(status.getCreatedAt());
				statusInfo.setStatusId(status.getId());
				statusInfo.setText(status.getText());
				statusInfo.setSource(status.getSource());
				statusInfo.setFavoriteCount(status.getFavoriteCount());
				if (status.getPlace() != null) {
					statusInfo.setAddress(status.getPlace().getFullName());
					statusInfo.setCountryCode(status.getPlace().getCountry());
				}
				statusInfo.setRetweetCount(status.getRetweetCount());
				if (status.getRetweetedStatus() != null) {
					statusInfo.setRetweetedStatusId(status.getRetweetedStatus().getId());
				}
				statusInfo.setUserId(status.getUser().getId());
				statusInfoes.add(statusInfo);
				logger.info("add statusInfo:" + statusInfo.getStatusId() + "," + statusInfo.getText() + "successful");
			}
			SINCE_ID = statuses.get(0).getId();
			System.out.println("sinceId=" + SINCE_ID);
			statuses = null;
		}
		return statusInfoes;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

}
