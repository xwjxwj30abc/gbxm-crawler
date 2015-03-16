package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.dao.GoogleDaoImpl;
import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.gbxm.google.domain.UserInfo;

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
		String userId = "110924633889503463658";
		while (true) {
			long currentTime = System.currentTimeMillis() - 17200_000;
			statusInfos = activityList.getActivitiesByUserId(userId, currentTime);
			logger.info("activities length=" + statusInfos.size());
			googleDaoImpl.insertStatusInfo(statusInfos);
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(userId);
			userInfo.setUserName("hjhj");
			userInfo.setLastUpdateTime(new Timestamp(currentTime));
			if (googleDaoImpl.isExisted("googleUserInfos", userId)) {
				googleDaoImpl.updatedUserInfo(userInfo);
			} else {
				googleDaoImpl.insertUserInfo(userInfo);
			}
			long spendTime = System.currentTimeMillis() - currentTime;
			Thread.sleep(currentTime + 3000_000 - spendTime);
			if (credential.refreshToken()) {
				plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(), credential)
						.setApplicationName("zxsoft crawler").build();
				activityList.setPlus(plus);
			}
		}
	}
}
