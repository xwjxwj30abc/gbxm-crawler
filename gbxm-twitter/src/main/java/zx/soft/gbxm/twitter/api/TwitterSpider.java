package zx.soft.gbxm.twitter.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import zx.soft.gbxm.twitter.dao.TwitterDaoImpl;
import zx.soft.gbxm.twitter.domain.PostData;
import zx.soft.gbxm.twitter.domain.RecordInfo;
import zx.soft.gbxm.twitter.domain.StatusInfo;
import zx.soft.gbxm.twitter.domain.Token;
import zx.soft.gbxm.twitter.domain.UserInfo;
import zx.soft.utils.checksum.CheckSumUtils;
import zx.soft.utils.config.ConfigUtil;
import zx.soft.utils.json.JsonUtils;

public class TwitterSpider {

	private static Logger logger = LoggerFactory.getLogger(TwitterSpider.class);
	private static TwitterDaoImpl twitterDaoImpl = new TwitterDaoImpl();
	private static final String URL = "http://36.7.150.150:18900/sentiment/index";
	private final ClientResource clientResource = new ClientResource(URL);
	static int i = 0;

	public int run(Follows follows) throws InterruptedException, TwitterException {

		List<StatusInfo> statusInfos = new ArrayList<>();
		PostData postData = new PostData();
		List<RecordInfo> records = new ArrayList<>();
		List<Status> statuses = follows.getHomeTimeLine();
		if (statuses.size() != 0) {
			for (Status status : statuses) {
				StatusInfo statusInfo = new StatusInfo();
				RecordInfo record = new RecordInfo();
				String url = "https://twitter.com/" + status.getUser().getName() + "/status/" + status.getId();
				record.setId(CheckSumUtils.getMD5(url).toUpperCase());//状态id,用户id进行MD5加密
				record.setMid(Long.toString(status.getId()));//主id
				record.setUsername(Long.toString(status.getUser().getId())); //用户id
				record.setNickname(status.getUser().getName()); //用户昵称

				if (status.getRetweetedStatus() != null) {
					record.setOriginal_id(Long.toString(status.getRetweetedStatus().getId())); //原创记录id
					record.setOriginal_uid(Long.toString(status.getRetweetedStatus().getUser().getId())); //原创用户id
					record.setOriginal_name(status.getRetweetedStatus().getUser().getName()); //原创用户昵称
					statusInfo.setRetweetedStatusId(status.getRetweetedStatus().getId());
					statusInfo.setRetweetedUserId(status.getRetweetedStatus().getUser().getId());
					statusInfo.setRetweetedUserName(status.getRetweetedStatus().getUser().getName());
				}
				record.setUrl(url);//url
				record.setContent(status.getText()); //该记录内容
				record.setFavorite_count(status.getFavoriteCount()); //收藏数
				record.setRepost_count(status.getRetweetCount());//转发数
				record.setTimestamp(status.getCreatedAt().getTime());//该记录发布时间
				long currentTime = System.currentTimeMillis();
				record.setLasttime(currentTime);//lasttime
				record.setUpdate_time(currentTime); //update_time
				record.setFirst_time(currentTime); //first_time
				if (status.getPlace() != null) {
					record.setLocation(status.getPlace().getFullName());//该记录发布的地理位置信息
					statusInfo.setPlacename(status.getPlace().getFullName());
				}
				statusInfo.setId(status.getId());
				statusInfo.setUserId(status.getUser().getId());
				statusInfo.setUsername(status.getUser().getName());
				statusInfo.setText(status.getText());
				statusInfo.setFavoriteCount(status.getFavoriteCount());
				statusInfo.setRetweetCount(status.getRetweetCount());
				statusInfo.setCreatedAt(Long.toString(status.getCreatedAt().getTime()));

				records.add(record);
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
			postData.setNum(records.size());
			postData.setRecords(records);
			Representation entity = new StringRepresentation(JsonUtils.toJsonWithoutPretty(postData));
			entity.setMediaType(MediaType.APPLICATION_JSON);
			try {
				Representation representation = clientResource.post(entity);
				logger.info("post return " + representation.toString());
				Response response = clientResource.getResponse();
				logger.info(response.getEntityAsText());
			} catch (ResourceException e) {
				logger.error("post data to solr error ,now store data to mysql ");
				logger.error(e.getMessage());
				twitterDaoImpl.insertStatusInfo(statusInfos);
			}
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

	public static void main(String[] args) throws InterruptedException, IOException {
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
