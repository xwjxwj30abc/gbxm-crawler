package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.dao.GoogleDaoImpl;
import zx.soft.gbxm.google.domain.GooglePlusStatus;
import zx.soft.gbxm.google.domain.GoogleToken;
import zx.soft.gbxm.google.domain.PostData;
import zx.soft.gbxm.google.domain.RecordInfo;
import zx.soft.utils.checksum.CheckSumUtils;
import zx.soft.utils.json.JsonUtils;
import zx.soft.utils.time.TimeUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;

public class GoogleSpider {

	private static Logger logger = LoggerFactory.getLogger(GoogleSpider.class);
	private static final String URL = "http://36.7.150.150:18900/persist";
	private static final ClientResource clientResource = new ClientResource(URL);
	static int i = 0;
	public static GoogleDaoImpl googleDaoImpl = new GoogleDaoImpl();
	private static CredentialFile credentialFile;
	private String userInfoTableName;
	private long lastUpdateTme;
	private Plus plus;

	public GoogleSpider() {
		credentialFile = new CredentialFile();
		userInfoTableName = "googleUserInfos";
	}

	private static int getTableCount() {
		return googleDaoImpl.getTableCount("gplusApps");
	}

	private static GoogleToken getNextGoogleToken() {
		if (i == getTableCount()) {
			i = 0;
		}
		return googleDaoImpl.getGoogleTokenById("gplusApps", ++i);
	}

	public void run(Credential credential) throws IOException, InterruptedException {
		while (true) {
			try {
				if (credential.refreshToken()) {
					plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(),
							credential).setApplicationName("zxsoft crawler").build();
					logger.info("进行token更新");
				}
			} catch (IOException e) {
				logger.error("更新token失败");
				e.printStackTrace();
			}
			ActivityList activityList = new ActivityList(plus);
			List<String> userIdList = googleDaoImpl.getUserIdList(userInfoTableName);
			logger.info("待更新用户数量" + userIdList.size());
			long currentTime = System.currentTimeMillis();
			for (String userId : userIdList) {
				ArrayList<GooglePlusStatus> googlePlusStatuses = new ArrayList<>();
				lastUpdateTme = googleDaoImpl.getLastUpdateTimeByUserId(userInfoTableName, userId).getTime();
				googlePlusStatuses = activityList.getActivitiesByUserId(userId, lastUpdateTme);
				if (googlePlusStatuses.size() > 0) {
					List<RecordInfo> records = new ArrayList<>();
					for (GooglePlusStatus googlePlusStatus : googlePlusStatuses) {
						RecordInfo record = new RecordInfo();
						record.setId(CheckSumUtils.getMD5(googlePlusStatus.getUrl()).toUpperCase());
						record.setMid(googlePlusStatus.getId());
						record.setUsername(googlePlusStatus.getActor_id());
						record.setNickname(googlePlusStatus.getActor_display_name());
						record.setOriginal_id(googlePlusStatus.getObject_id());
						record.setOriginal_uid(googlePlusStatus.getObject_actor_id());
						record.setOriginal_name(googlePlusStatus.getObject_actor_display_name());
						record.setOriginal_url(googlePlusStatus.getObject_url());
						record.setUrl(googlePlusStatus.getUrl());
						record.setTitle(googlePlusStatus.getTitle());
						record.setContent(googlePlusStatus.getObject_original_content());
						record.setComment_count(googlePlusStatus.getObject_replies_totalitems());
						record.setRepost_count(googlePlusStatus.getObject_resharers_totalitems());
						record.setAttitude_count(googlePlusStatus.getObject_plusoners_totalitems());
						record.setTimestamp(TimeUtils.transTimeLong(googlePlusStatus.getPublished()));
						record.setLasttime(currentTime);
						record.setFirst_time(currentTime);
						record.setUpdate_time(TimeUtils.transTimeLong(googlePlusStatus.getUpdated()));
						record.setLocation(googlePlusStatus.getPlace_name());
						record.setGeo(googlePlusStatus.getLatitude() + " " + googlePlusStatus.getLongitude());
						records.add(record);
						logger.info("add one record");
					}
					PostData data = new PostData();
					data.setNum(records.size());
					data.setRecords(records);
					Representation entity = new StringRepresentation(JsonUtils.toJsonWithoutPretty(data));
					entity.setMediaType(MediaType.APPLICATION_JSON);
					try {
						Representation representation = clientResource.post(entity);
						Response response = clientResource.getResponse();
						try {
							logger.info(response.getEntity().getText());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (ResourceException e) {
						logger.error("post data to solr error");
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				if (googleDaoImpl.isExisted(userInfoTableName, userId)) {
					googleDaoImpl.updatedUserInfo(userId, new Timestamp(currentTime));
					logger.info("更新" + userId + "检测时间");
				}
			}

			long spendTime = System.currentTimeMillis() - currentTime;
			logger.info("开始睡眠" + (3500_000 - spendTime) + "毫秒");
			Thread.sleep(3000_000 - spendTime);
			logger.info("睡眠结束");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		GoogleSpider googleSpider = new GoogleSpider();
		CredentialFile credentialFile = new CredentialFile();
		Credential credential = null;
		GoogleToken googleToken = getNextGoogleToken();
		try {
			credential = credentialFile.loadCredential(googleToken.getClient_id(), googleToken.getClient_secret(),
					googleToken.getApp_name(), "user");
		} catch (IOException e) {
			logger.error("加载credential文件错误");
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				googleSpider.run(credential);
			} catch (IOException e) {
				//如果是内部服务器错误，重新获取一次
				if (e.getMessage().contains("500")) {
					logger.error("500 Internal Server Error.");
					try {
						googleSpider.run(credential);
					} catch (IOException e1) {
						logger.error("again 500 Internal Server Error.");
						e1.printStackTrace();
					}
				}
				//连接超时
				if (e.getMessage().contains("connect timed out")) {
					logger.error("connect timed out");
				}
				//应用的请求次数限制导致错误，更换应用
				if (e.getMessage().contains("Daily Limit Exceeded")) {
					logger.error("Daily Limit Exceeded");
					logger.info("应用的请求次数限制导致错误，更换应用");
					googleToken = getNextGoogleToken();
					try {
						credential = credentialFile.loadCredential(googleToken.getClient_id(),
								googleToken.getClient_secret(), googleToken.getApp_name(), "user");
					} catch (IOException e1) {
						logger.error("load credential file error");
						e1.printStackTrace();
					} catch (GeneralSecurityException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
