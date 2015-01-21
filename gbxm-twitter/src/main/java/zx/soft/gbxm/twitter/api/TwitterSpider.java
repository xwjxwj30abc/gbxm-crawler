package zx.soft.gbxm.twitter.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
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
		twitterApi.getTwitter().setOAuthAccessToken(getSpecToken(tokenId));
		try {
			statuses = twitterApi.getHomeTimeLine();
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
		} catch (TwitterException e) {
			logger.info("try next token");
			tokenId++;
			return -1;
		}
	}

	/**
	 * 通过id获得数据库中的accessToken以及sinceId
	 * @param id
	 * @return
	 */
	private AccessToken getSpecToken(long id) {
		Token token = twitterDaoImpl.getToken("twitterTokens", id);
		AccessToken changedAccessToken = new AccessToken(token.getTokenkey(), token.getTokensecret());
		return changedAccessToken;
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
