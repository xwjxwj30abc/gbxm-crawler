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
import zx.soft.gbxm.google.domain.GoogleToken;
import zx.soft.gbxm.google.domain.PostData;
import zx.soft.gbxm.google.domain.RecordInfo;
import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.utils.checksum.CheckSumUtils;
import zx.soft.utils.json.JsonUtils;

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
				ArrayList<StatusInfo> statusInfos = new ArrayList<>();
				lastUpdateTme = googleDaoImpl.getLastUpdateTimeByUserId(userInfoTableName, userId).getTime();
				statusInfos = activityList.getActivitiesByUserId(userId, lastUpdateTme);
				if (statusInfos.size() > 0) {
					List<RecordInfo> records = new ArrayList<>();
					for (StatusInfo statusInfo : statusInfos) {
						RecordInfo record = new RecordInfo();
						record.setId(CheckSumUtils.getMD5(statusInfo.getUrl()).toUpperCase());
						record.setMid(statusInfo.getId());
						record.setUsername(statusInfo.getActorDisplayName());
						record.setNickname(statusInfo.getActorFamilyNamegivenName());
						record.setOriginal_id(statusInfo.getObjectId());
						record.setOriginal_uid(statusInfo.getObjectActorId());
						record.setOriginal_name(statusInfo.getObjectActorDisplayName());
						record.setOriginal_url(statusInfo.getObjectUrl());
						record.setUrl(statusInfo.getUrl());
						record.setHome_url(statusInfo.getActorUrl());
						record.setTitle(statusInfo.getTitile());
						record.setContent(statusInfo.getObjectContent());
						record.setComment_count(statusInfo.getRepliesTotalItems());
						record.setRepost_count(statusInfo.getResharersTotalItems());
						record.setTimestamp(statusInfo.getPublished().getTime());
						record.setLasttime(currentTime);
						record.setFirst_time(currentTime);
						record.setUpdate_time(statusInfo.getUpdated().getTime());
						record.setLocation(statusInfo.getPlaceName());
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
						logger.error("post data to solr error ,now insert into mysql");
						logger.error(e.getMessage());
						googleDaoImpl.insertStatusInfo(statusInfos);
					}
				}
				if (googleDaoImpl.isExisted(userInfoTableName, userId)) {
					googleDaoImpl.updatedUserInfo(userId, new Timestamp(currentTime));
					logger.info("更新" + userId + "检测时间");
				}
			}

			long spendTime = System.currentTimeMillis() - currentTime;
			logger.info("开始睡眠" + (3000_000 - spendTime) + "毫秒");
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
						logger.info("again 500 Internal Server Error.");
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
