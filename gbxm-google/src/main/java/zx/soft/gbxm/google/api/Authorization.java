package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.PlusScopes;

public class Authorization {
	private static Logger logger = LoggerFactory.getLogger(Authorization.class);
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/plus_sample");
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static GoogleAuthorizationCodeFlow flow;
	static GoogleAuthorizationCodeFlow.Builder flowBuilder;

	public static Credential authorize(String userId) throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
				PlusSample.class.getResourceAsStream("/client_secrets.json")));
		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory)
				.addRefreshListener(new DataStoreCredentialRefreshListener(userId, dataStoreFactory)).build();
		Builder builder = new Builder().setHost("localhost").setPort(8080);
		return new AuthorizationCodeInstalledApp(flow, builder.build()).authorize(userId);
	}

	public static void main(String[] args) {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			while (true) {
				logger.info("现在token过期时间：" + new Date(authorize("test1").getExpirationTimeMilliseconds()));
				Thread.sleep(60_000);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
