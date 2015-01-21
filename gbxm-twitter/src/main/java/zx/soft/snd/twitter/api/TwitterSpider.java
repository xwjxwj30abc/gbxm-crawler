package zx.soft.snd.twitter.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;
import zx.soft.snd.twitter.domain.UserInfo;

public class TwitterSpider {

	private static Logger logger = LoggerFactory.getLogger(TwitterSpider.class);
	private TwitterApi twitterApi;
	private TwitterDaoImpl twitterDaoImpl;

	private static long tokenId = 1L;

	public TwitterSpider() {
		twitterApi = new TwitterApi();
		twitterDaoImpl = new TwitterDaoImpl();
	}

	/**
	 * 插入获得的所有关注用户的更新信息到数据库并更新token 中的sinceId
	 * @throws InterruptedException
	 * @throws TwitterException
	 * @throws UnsupportedEncodingException
	 */
	public int run() throws InterruptedException {
		List<Status> statuses = null;
		List<StatusInfo> statusInfos = new ArrayList<>();
		getSpecToken(tokenId);
		try {
			statuses = twitterApi.getHomeTimeLine();
			twitterDaoImpl.updateSinceId(TwitterApi.SINCEID, tokenId);
			tokenId++;
		} catch (TwitterException e) {
			/*getSpecToken(++tokenId);
			try {
				statuses = twitterApi.getHomeTimeLine();
				twitterDaoImpl.updateSinceId(TwitterApi.SINCEID, tokenId);
				tokenId++;
			} catch (TwitterException e1) {
				logger.info("use up all the tokens...start sleep 15 min");
				Thread.sleep(900000);
			}*/
			logger.info("try next token");
			tokenId++;
		}

		if (statuses != null && statuses.size() != 0) {
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
		}
		statuses = null;
		twitterDaoImpl.insertStatusInfo(statusInfos);
		return statusInfos.size();
	}

	/**
	 * 自动关注数据表中的用户(考虑每一个twitter帐号关注对应的一个表中所有用户)
	 */
	public void autoFollow() {
		String tableName = "twitterUserName";
		getSpecToken(tokenId++);
		long userNameCount = twitterDaoImpl.getTableCount(tableName);
		logger.info("userNameCount=" + userNameCount);
		for (int i = 0; i < (userNameCount / 100) + 1; i++) {
			List<String> userNames = twitterDaoImpl.getNameList(tableName, i * 100);
			for (String userName : userNames) {
				try {
					twitterApi.autoFollow(userName);
					logger.info("follow" + userName.toString() + "successful");
				} catch (TwitterException e) {
					if (e.getErrorCode() == 161) {
						logger.error("Exception:{},StackTrace:{}", e.getMessage(), e.getStackTrace());
						logger.info("unable to follow more .change token and try follow again...");
						getSpecToken(tokenId++);
						try {
							twitterApi.autoFollow(userName);
							logger.info("follow" + userName.toString() + "successful");
						} catch (TwitterException e1) {
							logger.error("Exception:{},StackTrace:{}", e.getMessage(), e.getStackTrace());
						}
					}
				}

			}
		}
	}

	/**
	 * 通过id获得数据库中的accessToken以及sinceId
	 * @param id
	 * @return
	 */
	private Twitter getSpecToken(long id) {
		Token token = twitterDaoImpl.getToken("twitterTokens", id);
		AccessToken changedAccessToken = new AccessToken(token.getTokenkey(), token.getTokensecret());
		twitterApi.getTwitter().setOAuthAccessToken(changedAccessToken);
		TwitterApi.SINCEID = token.getSinceId();
		return twitterApi.getTwitter();
	}

	/**
	 * 获得token表的数量
	 * @param tokenTable
	 * @return
	 */
	public long getTokenCount(String tokenTable) {
		return twitterDaoImpl.getTableCount(tokenTable);
	}

	public static void main(String[] args) throws InterruptedException {

		TwitterSpider spider = new TwitterSpider();
		int statusSize = 0;
		while (true) {
			if (TwitterSpider.tokenId > spider.getTokenCount("twitterTokens")) {
				TwitterSpider.tokenId = 1L;
			}
			statusSize = spider.run();
			if (statusSize == 0) {
				Thread.sleep(60000);
			}

		}
	}
}
