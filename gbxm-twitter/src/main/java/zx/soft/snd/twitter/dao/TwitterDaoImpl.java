package zx.soft.snd.twitter.dao;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;
import zx.soft.snd.twitter.domain.UserInfo;
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

	//从数据库表中获取指定id的Token;
	public Token getToken(String tableName, long id) {
		Token token = null;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			TwitterDao dao = session.getMapper(TwitterDao.class);
			token = dao.getToken(tableName, id);
			logger.info("get token id = " + token.toString());
		}
		return token;
	}

	//获取表的长度
	public long getTableCount(String tablename) {
		long count = 0;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao dao = sqlSession.getMapper(TwitterDao.class);
			count = dao.getTableCount(tablename);
			//logger.info(tablename + "\'s length=" + count);
		}
		return count;
	}

	/**
	 * 判断用户存在与否
	 */
	public boolean isUserExisted(String tablename, long userId) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao dao = sqlSession.getMapper(TwitterDao.class);
			if (dao.getIdByUserId(tablename, userId) == null) {
				return Boolean.FALSE;
			} else {
				return Boolean.TRUE;
			}
		}
	}

	/**
	 * 插入状态信息
	 * @param statusInfoes
	 * @throws UnsupportedEncodingException
	 */
	public void insertStatusInfo(List<StatusInfo> statusInfoes) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			for (StatusInfo statusInfo : statusInfoes) {
				try {
					twitterDao.insertStatusInfo(statusInfo);
					//logger.info("insert statusInfo" + statusInfo.getText() + "successful");
				} catch (Exception e) {
					try {
						statusInfo.setText(new String(statusInfo.getText().getBytes(), "GBK"));
						twitterDao.insertStatusInfo(statusInfo);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 插入用户信息到数据库twitterUserInfos
	 * @param name
	 */
	public void insertUserInfo(UserInfo userInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.insertUserInfo(userInfo);
			//logger.info("insert userInfo:" + userInfo.toString() + "  successful");
		}
	}

	/**
	 * 更新用户信息到数据库twitterUserInfos
	 * @param userInfoes
	 */
	public void updateUserInfos(UserInfo userInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.updateUserInfo(userInfo);
			//logger.info("update userInfo:" + userInfo.toString() + "  successful");
		}
	}

	/**
	 * 更新Token sinceId到数据库twitterTokens
	 * @param userInfoes
	 */
	public void updateSinceId(long sinceId, int id) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.updateToken(sinceId, id);
			logger.info("update sinceId:" + sinceId + "where TokenId=" + id + "  successful");
		}
	}

	public void insertToken(Token token) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			TwitterDao twitterDao = sqlSession.getMapper(TwitterDao.class);
			twitterDao.insertToken(token);
			logger.info("insert token:" + token.toString() + "  successful");
		}
	}

}
