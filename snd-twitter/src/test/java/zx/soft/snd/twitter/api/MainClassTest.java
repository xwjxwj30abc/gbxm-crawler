package zx.soft.snd.twitter.api;

import org.junit.Test;

import twitter4j.TwitterException;

public class MainClassTest {

	//@Ignore
	@Test
	public void testinsertStatusInfos() throws TwitterException, InterruptedException {
		MainClass mainClass = new MainClass();
		while (true) {
			mainClass.insertStatusInfos();
			Thread.currentThread().sleep(60000);
		}
	}
}
