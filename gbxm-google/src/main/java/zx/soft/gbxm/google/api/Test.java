package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Activities.List;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class Test {

	public static void main(String[] args) throws IOException, GeneralSecurityException {

		CredentialFile credentialFile = new CredentialFile();
		String credentialFilePath = ".store/plus_sample";
		Credential credential = credentialFile.loadCredential(credentialFilePath, "test");
		System.out.println(credential.getAccessToken());
		System.out.println(credential.getRefreshToken());
		System.out.println(credential.getExpiresInSeconds());
		System.out.println(credential.refreshToken());
		System.out.println(credential.getAccessToken());
		System.out.println(credential.getRefreshToken());
		System.out.println(credential.getExpiresInSeconds());
		Plus plus = new Plus.Builder(credentialFile.getHttpTransport(), CredentialFile.getJsonFactory(), credential)
		.setApplicationName("zxsoft crawler").build();
		View.header1("Listing  Activities");
		//获取第一页活动状态信息
		List listActivities = plus.activities().list("110924633889503463658", "public");
		System.out.println("list activities length=" + listActivities.size());
		System.out.println("list fields=" + listActivities.getUserId());
		listActivities.setMaxResults(2L);
		listActivities.setFields("nextPageToken,items(published,id,url,object/content)");
		ActivityFeed feed = listActivities.execute();
		int currentPageNumber = 0;
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 2) {
			int i = 1;
			for (Activity activity : feed.getItems()) {
				//View.show(activity);
			}
			// 获取下一页
			String nextPageToken = feed.getNextPageToken();
			if (nextPageToken == null) {
				break;
			}
			listActivities.setPageToken(nextPageToken);
			View.header2("New page of activities");
			feed = listActivities.execute();
		}
	}
}
