package zx.soft.snd.twitter.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class CollectId {

	private static Logger logger = LoggerFactory.getLogger(CollectId.class);

	public boolean isRateLimitExceeded = false;

	public CollectId() {
	}

	/*
	 * 由第一层获得的ids 获取每一个id用户的FriendsIDs即第二层ids;
	 */
	public Queue<String> getUserFriendsIDs(Twitter twitter, String userId) {
		long cursor = -1;
		Queue<String> idsList = new LinkedList<>();
		IDs ids = null;
		String specificId;
		try {
			do {
				ids = twitter.getFriendsIDs(Long.valueOf(userId), cursor);
				for (int j = 0; j < ids.getIDs().length; j++) {
					specificId = String.valueOf(ids.getIDs()[j]);
					idsList.add(specificId);
				}
			} while ((cursor = ids.getNextCursor()) != 0);

		} catch (TwitterException e) {
			logger.error("Message:{},StackTrack:{}", e.getMessage(), e.getStackTrace());
			isRateLimitExceeded = true;
		}
		return idsList;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IllegalStateException, TwitterException, InterruptedException {

		List<AccessToken> accessTokens = new ArrayList<>();
		int operationTimes = 0;
		int index = 0;
		AccessToken accessToken1 = new AccessToken("2960145383-twSYPbJwZ3uGibd014Qkd1TWBguMl4zutGrPvw9",
				"l2RMi0w6Rw67mnXMJZqVJ6qxTzv4cW2qLajkMd136F8FP");
		AccessToken accessToken2 = new AccessToken("2490910454-idLUwxLp96F1GRWhDh1uKelEdYbL9lr80HbtVoY",
				"KOf18jCAfes8VIkNmekzI2pAh8pbbUQ5cpfZ3bCNweiVd");
		AccessToken accessToken3 = new AccessToken("2922413937-5w4Ss2sg272Kje8z6G3RdDdRLxZ1oB1JQv2AWPm",
				"KcFj1fJ9ZccUwRrUgaqam0SwWSqKbxRdqBDmVhwM4iCtY");
		accessTokens.add(accessToken1);
		accessTokens.add(accessToken2);
		accessTokens.add(accessToken3);
		CollectId collectId = new CollectId();
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("I57Q98TSGB7Lay64XMIFtjbp4", "X8lMgSSqf9Q2bKjMb9cn3FyImnow1bXzLCVqyyHe0W1UAnajvG");

		long startTime = System.currentTimeMillis();
		twitter.setOAuthAccessToken(accessTokens.get(index++));

		Queue<String> idsList = null;
		idsList = collectId.getUserFriendsIDs(twitter, String.valueOf(twitter.getId()));
		operationTimes++;
		while (collectId.isRateLimitExceeded == false) {
			if (operationTimes < 13) {
				Queue<String> anotherIdList = new LinkedList<>();
				String head = idsList.poll();
				logger.info("获得队列head的Id: " + head);
				anotherIdList = collectId.getUserFriendsIDs(twitter, head);
				operationTimes++;
				logger.info("anotherIdListLength=" + anotherIdList.size());
				logger.info("current idsListLength= " + idsList.size());
				idsList.addAll(anotherIdList);
				logger.info("current idsListLength= " + idsList.size());
			} else {
				twitter.setOAuthAccessToken(accessTokens.get(index++));
				operationTimes = 0;
				if (index == (accessTokens.size())) {
					index = 0;
					Thread.currentThread().sleep(900000);
				}
			}
		}
		logger.info("when rateLimitExceeded the idsLengths=" + idsList.size());
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
	}
}
