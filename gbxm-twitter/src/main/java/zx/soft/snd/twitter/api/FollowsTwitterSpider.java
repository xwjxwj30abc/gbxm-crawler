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
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.UserInfo;
import zx.soft.utils.config.ConfigUtil;

public class FollowsTwitterSpider {
	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private TwitterDaoImpl twitterDaoImpl;
	private static Twitter twitter;
	private static final int COUNT = 200;//每页的数量,取最大值200
	private static int page = 1;//获取第一页信息
	private static long sinceId = 1L;

	public FollowsTwitterSpider(AccessToken accessToken) {
		twitterDaoImpl = new TwitterDaoImpl();
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
		twitter.setOAuthAccessToken(accessToken);
	}

	/**
	 * 获取twitter用户的主页动态信息
	 * @return
	 * @throws TwitterException
	 * @throws InterruptedException
	 */
	private List<Status> getHomeTimeLine() {
		List<Status> statuses = new ArrayList<>();
		boolean flag = true;
		long lastTimeSinceId = sinceId;
		while (flag) {
			try {
				Paging nextPaging = new Paging(page++, COUNT, lastTimeSinceId);
				ResponseList<Status> nextStatuses = twitter.getHomeTimeline(nextPaging);
				logger.info("page=" + page + ",size=" + nextStatuses.size());
				if (nextStatuses.size() == 0) {
					flag = false;
				} else {
					statuses.addAll(nextStatuses);
					sinceId = statuses.get(0).getId();
					nextStatuses = null;
				}
			} catch (TwitterException e) {
				logger.info("page too long...start sleep 15min");
				try {
					Thread.sleep(900000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				page--;
			}
		}
		page = 1;
		return statuses;
	}

	/**
	 * 获得状态信息插入状态数据库，并更新用户信息
	 * @return  状态信息的长度
	 * @throws InterruptedException
	 */
	public int run() throws InterruptedException {
		List<StatusInfo> statusInfos = new ArrayList<>();
		List<Status> statuses = getHomeTimeLine();
		if (statuses.size() != 0) {
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
				statusInfos.add(statusInfo);

				UserInfo userInfo = new UserInfo();
				User user = status.getUser();
				userInfo.setUserId(user.getId());
				userInfo.setName(user.getName());
				userInfo.setScreenName(user.getScreenName());
				userInfo.setLocation(user.getLocation());
				if (user.getDescription() != null) {
					userInfo.setDescription(user.getDescription());
				}
				if (user.getURL() != null) {
					userInfo.setUrl(user.getURL());
				}
				userInfo.setFollowersCount(user.getFollowersCount());
				userInfo.setFriendsCount(user.getFriendsCount());
				userInfo.setCreatedAt(user.getCreatedAt());
				userInfo.setFavouritesCount(user.getFavouritesCount());
				userInfo.setListedCount(user.getListedCount());
				if (user.getStatus() != null) {
					userInfo.setLastStatusId(user.getStatus().getId());
				}
				userInfo.setStatusesCount(user.getStatusesCount());
				if (twitterDaoImpl.isUserExisted("twitterUserInfos", userInfo.getUserId())) {
					twitterDaoImpl.updateUserInfos(userInfo);
				} else {
					twitterDaoImpl.insertUserInfo(userInfo);
				}
			}
			statuses = null;
			twitterDaoImpl.insertStatusInfo(statusInfos);
		}
		return statusInfos.size();
	}

}
