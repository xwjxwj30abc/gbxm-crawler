package zx.soft.gbxm.google.api;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
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

import zx.soft.gbxm.google.dao.GoogleDaoImpl;
import zx.soft.gbxm.google.domain.PostData;
import zx.soft.gbxm.google.domain.RecordInfo;
import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.utils.checksum.CheckSumUtils;
import zx.soft.utils.config.ConfigUtil;
import zx.soft.utils.json.JsonUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;

public class GoogleSpider {

	private static Logger logger = LoggerFactory.getLogger(GoogleSpider.class);
	private static Properties props = ConfigUtil.getProps("client_secrets.properties");
	private static final String URL = "http://36.7.150.150:18900/sentiment/index";
	private static final ClientResource clientResource = new ClientResource(URL);

	public static void main(String[] args) throws GeneralSecurityException, InterruptedException {
		GoogleDaoImpl googleDaoImpl = new GoogleDaoImpl();
		CredentialFile credentialFile = new CredentialFile();
		String[] childFiles = new File(props.getProperty("credential_path")).list();
		Credential credential = null;
		try {
			credential = credentialFile.loadCredential(childFiles[0], "test");
		} catch (IOException e2) {
			logger.info("加载credential文件错误");
			e2.printStackTrace();
		}
		Plus plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(), credential)
		.setApplicationName("zxsoft crawler").build();
		ActivityList activityList = new ActivityList(plus);
		ArrayList<StatusInfo> statusInfos = new ArrayList<>();
		String userInfoTableName = "googleUserInfos";
		while (true) {
			List<String> userIdList = googleDaoImpl.getUserIdList(userInfoTableName);
			logger.info("待更新用户数量" + userIdList.size());
			long currentTime = System.currentTimeMillis();
			long lastUpdateTme = 0;
			for (String userId : userIdList) {
				logger.info("get current userId=" + userId);
				lastUpdateTme = googleDaoImpl.getLastUpdateTimeByUserId(userInfoTableName, userId).getTime();
				try {
					statusInfos = activityList.getActivitiesByUserId(userId, lastUpdateTme);
				} catch (IOException e) {
					if (e.getMessage().contains("500")) {
						logger.error("500 Internal Server Error.");
						try {
							statusInfos = activityList.getActivitiesByUserId(userId, lastUpdateTme);
						} catch (IOException e1) {
							logger.info("again 500 Internal Server Error.");
							e1.printStackTrace();
						}
					}
				}
				logger.info("activities length=" + statusInfos.size());
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
					}
					PostData data = new PostData();
					data.setNum(records.size());
					data.setRecords(records);
					Representation entity = new StringRepresentation(JsonUtils.toJsonWithoutPretty(data));
					entity.setMediaType(MediaType.APPLICATION_JSON);
					try {
						Representation representation = clientResource.post(entity);
						logger.info("post return " + representation.toString());
						Response response = clientResource.getResponse();
						logger.info(response.getEntityAsText());
					} catch (ResourceException e) {
						logger.error("post data to solr error ,now insert into mysql");
						logger.error(e.getMessage());
						googleDaoImpl.insertStatusInfo(statusInfos);
					}
					if (googleDaoImpl.isExisted(userInfoTableName, userId)) {
						googleDaoImpl.updatedUserInfo(userId, new Timestamp(currentTime));
					}
				}
			}
			long spendTime = System.currentTimeMillis() - currentTime;
			logger.info("开始睡眠" + (3000_000 - spendTime) + "毫秒");
			Thread.sleep(3000_000 - spendTime);
			logger.info("睡眠结束");
			try {
				if (credential.refreshToken()) {
					plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(),
							credential).setApplicationName("zxsoft crawler").build();
					activityList.setPlus(plus);
					logger.info("进行token更新");
				}
			} catch (IOException e) {
				logger.error("更新token失败");
				e.printStackTrace();
			}
		}
	}
}
