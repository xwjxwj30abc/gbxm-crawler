package zx.soft.gbxm.google.demo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Activities.List;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class PlusSample {

	private static Logger logger = LoggerFactory.getLogger(PlusSample.class);

	private static final String APPLICATION_NAME = "zxsoft crawler";

	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/plus_sample");

	private static FileDataStoreFactory dataStoreFactory;

	private static HttpTransport httpTransport;

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static Plus plus;

	private static DateTime lastUpdateTime = new DateTime(0);

	private static Credential authorize(String userId) throws IOException {
		/*	GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
					PlusSample.class.getResourceAsStream("/client_secrets.json")));
		*/
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				"804751998958-r10ohle1oem23jlgtff9npk68fh7v53i.apps.googleusercontent.com", "xSgLqQWNjNtj_RmsHfCdmYyG",
				Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory)
				.addRefreshListener(new DataStoreCredentialRefreshListener(userId, dataStoreFactory))
				.setApprovalPrompt("force").setAccessType("offline").build();
		Builder builder = new Builder().setHost("localhost").setPort(8080);
		return new AuthorizationCodeInstalledApp(flow, builder.build()).authorize(userId);
	}

	public static void main(String[] args) throws GeneralSecurityException, IOException {

		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		Credential credential = authorize("user");
		plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		listActivities("110924633889503463658");
		while (true) {
			updateActivities("110924633889503463658");
			credential = authorize("test");
			plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
					.build();
		}
	}

	/**
	 * 获取指定id用户的历史信息
	 * @param userId
	 * @throws IOException
	 */
	private static void listActivities(String userId) throws IOException {
		View.header1("Listing  Activities");
		//获取第一页活动状态信息
		List listActivities = plus.activities().list(userId, "public");
		listActivities.setMaxResults(2L);
		listActivities.setFields("nextPageToken,items(published,id,url,object/content)");
		ActivityFeed feed = listActivities.execute();
		lastUpdateTime = feed.getItems().get(0).getPublished();
		int currentPageNumber = 0;
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 2) {
			int i = 1;
			logger.info("currentPageNumber=  " + currentPageNumber);
			for (Activity activity : feed.getItems()) {
				logger.info("activity num=  " + i++);
				View.show(activity);
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

	/**
	 * 更新指定用户信息
	 * @param userId
	 * @throws IOException
	 */
	public static void updateActivities(String userId) throws IOException {

		List listActivities = plus.activities().list(userId, "public");
		listActivities.setMaxResults(2L);
		listActivities.setFields("nextPageToken,items(published,id,url,object/content)");
		ActivityFeed feed = listActivities.execute();
		int currentPageNumber = 0;
		long tempUpdateTime = lastUpdateTime.getValue();
		lastUpdateTime = feed.getItems().get(0).getPublished();
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 2) {
			if (feed.getItems().get(0).getPublished().getValue() <= tempUpdateTime) {
				break;
			}
			int i = 1;
			for (Activity activity : feed.getItems()) {
				logger.info("activity num=" + i++);
				View.show(activity);
			}
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
