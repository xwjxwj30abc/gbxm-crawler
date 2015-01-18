package zx.soft.snd.twitter.dao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;
import zx.soft.snd.web.utils.MybatisConfig;

public class TwitterDaoImpl {

	private Logger logger = LoggerFactory.getLogger(TwitterDaoImpl.class);
	private static SqlSessionFactory sqlSessionFactory;

	public TwitterDaoImpl() {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory();
	}

	//从数据库表中获取部分twitter用户名;
	public List<String> getNameList(String tableName, int from) {
		List<String> names = null;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			TwitterDao dao = session.getMapper(TwitterDao.class);
			names = dao.getNameList(tableName, from);
			logger.info("get name id from " + from);
			logger.info(names.toString());
		}
		return names;
	}

	//从数据库表中获取部分AccessToken;
	public List<Token> getTokenList(String tableName, int from) {
		List<Token> tokens = new ArrayList<>();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			TwitterDao dao = session.getMapper(TwitterDao.class);
			tokens = dao.getTokenList(tableName, from);
			logger.info("get name id from " + from);
			logger.info(tokens.toString());
		}
		return tokens;
	}

	//获取表的长度
	public int getTableCount(String tablename) {
		int count = 0;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao dao = sqlSession.getMapper(TwitterDao.class);
			count = dao.getTableCount(tablename);
			logger.info(tablename + "\'s length=" + count);
		}
		return count;
	}

	/**
	 * 插入状态信息
	 * @param statusInfo
	 * @throws UnsupportedEncodingException
	 */
	public void insertStatusInfo(StatusInfo statusInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.insertStatusInfo(statusInfo);
			logger.info("insert statusInfo" + statusInfo.getStatusId() + "successful");
		}
	}

	/**
	 * 插入用户名到数据库twitterUserName
	 * @param name
	 */
	public void insertName(String name) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.insertName(name);
			logger.info("insert " + name + "  successful");
		}
	}
}
