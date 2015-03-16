package zx.soft.gbxm.google.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialFile {

	private static Logger logger = LoggerFactory.getLogger(CredentialFile.class);

	public static List<String> getCredentialFileList(String path) {

		File file = new File(path);
		if (file.isDirectory()) {
			String[] fileNames = file.list();
			List<String> credentialFiles = new ArrayList<>();
			for (String fileName : fileNames) {
				credentialFiles.add(fileName);
				logger.info("read file  " + fileName + "  succeed!");
			}
			return credentialFiles;
		}
		return null;
	}

	public static void main(String[] args) {
		CredentialFile.getCredentialFileList("/home/fgq/Desktop");
	}
}
