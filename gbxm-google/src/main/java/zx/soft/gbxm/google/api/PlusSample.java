package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.utils.json.JsonUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
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

	private static DateTime lastUpdateTime;

	private static Credential authorize(String userId) throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
				PlusSample.class.getResourceAsStream("/client_secrets.json")));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory).build();
		Builder builder = new Builder().setHost("localhost").setPort(8080);
		return new AuthorizationCodeInstalledApp(flow, builder.build()).authorize(userId);
	}

	public static void main(String[] args) {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Credential credential = authorize("test");
			Date expirationTime = new Date(credential.getExpirationTimeMilliseconds());
			logger.info("token过期时间：" + credential.getExpirationTimeMilliseconds() + "equal  " + expirationTime);
			plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
					.build();
			listActivities("110924633889503463658");
			while (true) {
				updateActivities("110924633889503463658");
				Thread.sleep(3 * 60 * 1000);
				credential = authorize("tes");
				expirationTime = new Date(credential.getExpirationTimeMilliseconds());
				logger.info("token过期时间：" + expirationTime);
				plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
						.build();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
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
				logger.info(JsonUtils.toJsonWithoutPretty(activity));
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
				logger.info(JsonUtils.toJson(activity));
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
