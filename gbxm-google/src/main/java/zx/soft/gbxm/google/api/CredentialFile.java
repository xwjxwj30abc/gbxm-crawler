package zx.soft.gbxm.google.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.PlusScopes;

public class CredentialFile {

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static FileDataStoreFactory dataStoreFactory;
	private static File credentialFile;

	private static HttpTransport httpTransport;

	public Credential loadCredential(String credentialFilePath, String userId) throws IOException,
	GeneralSecurityException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		credentialFile = new File(System.getProperty("user.home"), credentialFilePath);
		dataStoreFactory = new FileDataStoreFactory(credentialFile);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
				PlusSample.class.getResourceAsStream("/client_secrets.json")));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory).build();
		Credential credential = flow.loadCredential(userId);
		if (credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
			if (credential.getRefreshToken() == null) {
				System.out.println("refresh TOken is null");
			}

			if (credential.getRefreshListeners() == null) {
				System.out.println("refresh token listerning is");
			}
			System.out.println(credential.getExpiresInSeconds());
			return credential;
		}
		return null;
	}

	public HttpTransport getHttpTransport() {
		return httpTransport;
	}

	public static void setHttpTransport(HttpTransport httpTransport) {
		CredentialFile.httpTransport = httpTransport;
	}

	public static JsonFactory getJsonFactory() {
		return JSON_FACTORY;
	}

	public static FileDataStoreFactory getDataStoreFactory() {
		return dataStoreFactory;
	}

	public static void setDataStoreFactory(FileDataStoreFactory dataStoreFactory) {
		CredentialFile.dataStoreFactory = dataStoreFactory;
	}

	public static File getCredentialFile() {
		return credentialFile;
	}

	public static void setCredentialFile(File credentialFile) {
		CredentialFile.credentialFile = credentialFile;
	}

}
