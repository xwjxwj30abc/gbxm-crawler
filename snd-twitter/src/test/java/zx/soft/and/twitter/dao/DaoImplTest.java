package zx.soft.and.twitter.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import zx.soft.snd.twitter.dao.TwitterDaoImpl;
import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;

public class DaoImplTest {

	//@Ignore
	@Test
	public void testgetNameList() {
		TwitterDaoImpl daoaImpl = new TwitterDaoImpl();
		List<String> names = new ArrayList<>();
		names = daoaImpl.getNameList("twitterUserName", 0);
		System.out.println("get names" + names.toString());
	}

	@Ignore
	@Test
	public void testgetTokenList() {
		TwitterDaoImpl daoImpl = new TwitterDaoImpl();
		List<Token> tokens = new ArrayList<>();
		tokens = daoImpl.getTokenList("twitterToken", 0);
		System.out.println("get names" + tokens.toString());
	}

	@Ignore
	@Test
	public void testTableCount() {
		TwitterDaoImpl daoImpl = new TwitterDaoImpl();
		int count = daoImpl.getTableCount("twitterUserName");
		Assert.assertEquals(2, count);
	}

	@Ignore
	@Test
	public void testinsertStatusInfo() {
		TwitterDaoImpl daoImpl = new TwitterDaoImpl();
		StatusInfo statusInfo = new StatusInfo();
		statusInfo.setStatusId(87519551L);
		statusInfo.setText("插入statusInfo信息");
		daoImpl.insertStatusInfo(statusInfo);
	}
}
