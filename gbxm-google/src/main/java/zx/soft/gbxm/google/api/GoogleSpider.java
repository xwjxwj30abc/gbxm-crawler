package zx.soft.gbxm.google.api;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.dao.GoogleDaoImpl;
import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.utils.config.ConfigUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;

public class GoogleSpider {

	private static Logger logger = LoggerFactory.getLogger(GoogleSpider.class);
	private static Properties props = ConfigUtil.getProps("client_secrets.properties");

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
				googleDaoImpl.insertStatusInfo(statusInfos);
				if (googleDaoImpl.isExisted(userInfoTableName, userId)) {
					googleDaoImpl.updatedUserInfo(userId, new Timestamp(currentTime));
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
