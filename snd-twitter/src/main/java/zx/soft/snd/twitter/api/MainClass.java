package zx.soft.snd.twitter.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.utils.log.LogbackUtil;

public class MainClass {

	private static Logger logger = LoggerFactory.getLogger(MainClass.class);
	private TwitterApi twitterApi;
	private TwitterDaoImpl twitterDaoImpl;

	public MainClass() {
		AccessToken accessToken = new AccessToken("2922413937-khq0Hh3Otv6AItaFNCtnWRVWWRjVnT4BoBjIxoI",
				"q4nWHrNsAMcwIjwiuQbJnpKpBo11fFk0lTg3zS1mQWFpe");
		twitterApi = new TwitterApi();
		twitterApi.getTwitter().setOAuthAccessToken(accessToken);
		twitterDaoImpl = new TwitterDaoImpl();
	}

	/**
	 * 自动关注数据表中的用户(考虑每一个twitter帐号关注对应的一个表中所有用户)
	 */
	public void autoFollow(String tableName) {
		int userNameCount = twitterDaoImpl.getTableCount(tableName);
		logger.info("userNameCount=" + userNameCount);
		for (int i = 0; i < (userNameCount / 100) + 1; i++) {
			List<String> userNames = twitterDaoImpl.getNameList(tableName, i * 100);
			for (String userName : userNames) {
				twitterApi.autoFollow(userName);
				logger.info(userName.toString());
			}
		}
	}

	/**
	 * 插入获得的所有关注用户的更新信息到数据库
	 * @throws TwitterException
	 * @throws UnsupportedEncodingException
	 */
	public void insertStatusInfos() throws TwitterException {
		List<StatusInfo> statusInfos = twitterApi.statusesHomeTimeline();
		logger.info(statusInfos.toString());
		for (StatusInfo statusInfo : statusInfos) {
			try {
				twitterDaoImpl.insertStatusInfo(statusInfo);
			} catch (Exception e) {
				try {
					statusInfo.setText(new String(statusInfo.getText().getBytes(), "GBK"));
					twitterDaoImpl.insertStatusInfo(statusInfo);
				} catch (UnsupportedEncodingException e1) {
					logger.error("UnsupportedEncodingException:{}", LogbackUtil.expection2Str(e1));
				}
			}
		}
	}

	public static void main(String[] args) throws TwitterException, InterruptedException {
		MainClass mainClass = new MainClass();
		while (true) {
			mainClass.insertStatusInfos();
			Thread.currentThread().sleep(60000);
		}
	}

}
