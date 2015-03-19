package zx.soft.gbxm.google.api;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.utils.config.ConfigUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.PlusScopes;

public class CredentialFile {

	private static Logger logger = LoggerFactory.getLogger(CredentialFile.class);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static FileDataStoreFactory dataStoreFactory;
	private static File credentialFile;
	private static Properties props = ConfigUtil.getProps("client_secrets.properties");
	private static HttpTransport httpTransport;

	/**
	 * 根据文件名获取该应用的credentials文件中指定授权用户的credentail
	 * @param credentialFilename
	 * @param userId
	 * @return Credential
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public Credential loadCredential(String credentialFilename, String userId) throws IOException,
			GeneralSecurityException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		credentialFile = new File(props.getProperty("credential_path") + credentialFilename);
		dataStoreFactory = new FileDataStoreFactory(credentialFile);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				props.getProperty("client_id"), props.getProperty("client_secret"),
				Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory).build();
		Credential credential = flow.loadCredential(userId);
		logger.info("token 在" + new Timestamp(credential.getExpirationTimeMilliseconds()).toString() + "时候过期");
		if (credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
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
