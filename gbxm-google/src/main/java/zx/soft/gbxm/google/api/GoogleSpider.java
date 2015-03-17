package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.dao.GoogleDaoImpl;
import zx.soft.gbxm.google.domain.StatusInfo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;

public class GoogleSpider {

	private static Logger logger = LoggerFactory.getLogger(GoogleSpider.class);

	public static void main(String[] args) throws IOException, GeneralSecurityException, InterruptedException {
		GoogleDaoImpl googleDaoImpl = new GoogleDaoImpl();
		CredentialFile credentialFile = new CredentialFile();
		String credentialFilePath = ".store/plus_sample";
		Credential credential = credentialFile.loadCredential(credentialFilePath, "test");
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
				statusInfos = activityList.getActivitiesByUserId(userId, lastUpdateTme);
				logger.info("activities length=" + statusInfos.size());
				googleDaoImpl.insertStatusInfo(statusInfos);
				if (googleDaoImpl.isExisted(userInfoTableName, userId)) {
					googleDaoImpl.updatedUserInfo(userId, new Timestamp(currentTime));
				}
			}
			long spendTime = System.currentTimeMillis() - currentTime;
			//logger.info("开始睡眠" + (3000_000 - spendTime) + "毫秒");
			//Thread.sleep(3000_000 - spendTime);
			logger.info("开始睡眠10秒");
			Thread.sleep(10_000);
			logger.info("睡眠结束");
			if (credential.refreshToken()) {
				plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(), credential)
				.setApplicationName("zxsoft crawler").build();
				activityList.setPlus(plus);
				logger.info("进行token更新");
			}
		}
	}
}
