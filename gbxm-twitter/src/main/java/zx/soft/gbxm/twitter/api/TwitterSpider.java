package zx.soft.gbxm.twitter.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;
import zx.soft.snd.twitter.domain.UserInfo;
import zx.soft.utils.config.ConfigUtil;

public class TwitterSpider {

	private static Logger logger = LoggerFactory.getLogger(TwitterSpider.class);
	private static TwitterDaoImpl twitterDaoImpl = new TwitterDaoImpl();
	static int i = 0;

	public int run(Follows follows) throws InterruptedException, TwitterException {
		List<StatusInfo> statusInfos = new ArrayList<>();
		List<Status> statuses = follows.getHomeTimeLine();
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
			logger.info("insert statusInfo" + statusInfos.size());
			//同时更新sinceId
			twitterDaoImpl.updateSinceId(statusInfos.get(0).getStatusId(), i);
		}
		return statusInfos.size();
	}

	public static Token getTokenAndSinceId() {
		if (i == getTableCount()) {
			i = 0;
		}
		return twitterDaoImpl.getToken("twitterTokens", ++i);
	}

	/**
	 * 获得token表长度
	 * @return
	 */
	public static long getTableCount() {
		return twitterDaoImpl.getTableCount("twitterTokens");
	}

	public static void main(String[] args) throws InterruptedException {
		Twitter twitter = new TwitterFactory().getInstance();
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		twitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
		Follows follows = new Follows(twitter);
		Token instance = getTokenAndSinceId();
		follows.setAccessToken(new AccessToken(instance.getTokenkey(), instance.getTokensecret()));
		follows.setSinceId(instance.getSinceId());
		TwitterSpider twitterSpider = new TwitterSpider();
		int[] diffLength = { 0, 0 };
		long sleepTime = 0L;
		int j = 0;
		int k = 0;
		while (true) {
			diffLength[0] = diffLength[1];
			try {
				diffLength[1] = twitterSpider.run(follows);
				k = 0;
				if (diffLength[1] == 0) {
					j++;
					instance = getTokenAndSinceId();
					AccessToken changeToken = new AccessToken(instance.getTokenkey(), instance.getTokensecret());
					follows.setAccessToken(changeToken);
					follows.setSinceId(instance.getSinceId());
					diffLength[0] = 0;
					diffLength[1] = 0;
				} else {
					j = 0;
				}
				if (j == getTableCount()) {
					logger.info("已获得所有用户的状态更新，进行睡眠");
					sleepTime = (long) (1000 - diffLength[0]) * 1000;
					Thread.sleep(sleepTime);
				}
			} catch (TwitterException e) {
				logger.info("token 过期，异常发生，更换token");
				k++;
				if (k == getTableCount()) {
					logger.info("所有token均过期，休眠15min...");
					Thread.sleep(15 * 60 * 1000);
				}
				//从数据库重新获取一个授权用户token并抓取该授权用户所关注帐号的用户数据
				instance = getTokenAndSinceId();
				AccessToken changeToken = new AccessToken(instance.getTokenkey(), instance.getTokensecret());
				follows.setAccessToken(changeToken);
				follows.setSinceId(instance.getSinceId());
				diffLength[0] = 0;
				diffLength[1] = 0;
			}
		}
	}
}
