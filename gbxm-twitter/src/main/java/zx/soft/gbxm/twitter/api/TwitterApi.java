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
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.Token;
import zx.soft.utils.config.ConfigUtil;

public class TwitterApi {
	public static Logger logger = LoggerFactory.getLogger(TwitterApi.class);
	private Twitter twitter;
	private static final int COUNT = 200;//每页的数量,取最大值200
	private static int PAGE = 1;//获取第一页信息
	private static long SINCEID = 1L;
	private static long lastTimeSinceId = 1L;
	private static long MAXID = 2000000000000000001L;
	public static long tokenId = 1L;
	private TwitterDaoImpl twitterDaoImpl;

	public TwitterApi() {
		Properties prop = ConfigUtil.getProps("oauthconsumer.properties");
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
		twitter.setOAuthAccessToken(getSpecToken(tokenId));
		twitterDaoImpl = new TwitterDaoImpl();
	}

	/**
	 * 获取twitter用户的主页动态信息
	 * @return
	 * @throws TwitterException
	 * @throws InterruptedException
	 */
	public List<Status> getHomeTimeLine() throws InterruptedException {
		boolean flag = true;
		lastTimeSinceId = SINCEID;
		MAXID = 2000000000L;//MAXID为一个很大的值
		List<Status> statuses = new ArrayList<>();
		Paging paging = new Paging(1, COUNT, lastTimeSinceId, MAXID);
		try {
			if (tokenId > (getTokenCount("twitterTokens") - 1)) {
				tokenId = 1L;
			}
			twitter.setOAuthAccessToken(getSpecToken(tokenId));
			statuses = twitter.getHomeTimeline(paging);
			while (statuses.size() == 0) {
				Thread.sleep(60000);
				statuses = twitter.getHomeTimeline(paging);
			}
			SINCEID = statuses.get(0).getId();
			MAXID = statuses.get(0).getId();
			logger.info("update sinceId=" + SINCEID);
		} catch (TwitterException e) {
			twitter.setOAuthAccessToken(getSpecToken(++tokenId));
			getHomeTimeLine();
		}
		while (flag) {
			PAGE++;
			Paging nextPaging = new Paging(PAGE, COUNT, lastTimeSinceId, MAXID);
			ResponseList<Status> tempStatuses = null;
			try {
				tempStatuses = twitter.getHomeTimeline(nextPaging);
			} catch (TwitterException e) {
				twitter.setOAuthAccessToken(getSpecToken(++tokenId));
				try {
					tempStatuses = twitter.getHomeTimeline(nextPaging);
				} catch (TwitterException e1) {
					Thread.sleep(900000);
					twitter.setOAuthAccessToken(getSpecToken(++tokenId));
					try {
						tempStatuses = twitter.getHomeTimeline(nextPaging);
					} catch (TwitterException e2) {
						e2.printStackTrace();
					}
				}
			}
			if (tempStatuses.size() == 0) {
				flag = false;
			} else {
				statuses.addAll(tempStatuses);
				tempStatuses = null;
			}
		}
		return statuses;
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
	private long getTokenCount(String tokenTable) {
		return twitterDaoImpl.getTableCount(tokenTable);
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
