package zx.soft.gbxm.facebook.dao;

import java.sql.Timestamp;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.facebook.utils.MybatisConfig;

public class FBDaoImpl {

	private static Logger logger = LoggerFactory.getLogger(FBDaoImpl.class);

	private SqlSessionFactory sqlSessionFactory;

	public FBDaoImpl() {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory();
	}

	/**
	 * 获得facebook表中指定id的facebook token
	 * @param tablename
	 * @param id
	 * @return
	 */
	public String getFBToken(String tablename, int id) {
		String token = null;
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FBDao fbDao = sqlSession.getMapper(FBDao.class);
			token = fbDao.getToken(tablename, id);
		}
		return token;
	}

	/**
	 * 获得指定id的token 过期时间
	 * @param tablename
	 * @param id
	 * @return
	 */
	public Timestamp getExpireTime(String tablename, int id) {
		Timestamp expireTime = null;
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FBDao fbDao = sqlSession.getMapper(FBDao.class);
			expireTime = fbDao.getExpireTime(tablename, id);
		}
		return expireTime;
	}

	public int getTableLength(String tablename) {
		int length = 0;
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FBDao fbDao = sqlSession.getMapper(FBDao.class);
			length = fbDao.getTableLength(tablename);
		}
		return length;
	}

	public static void main(String[] args) {
		FBDaoImpl fbDaoImpl = new FBDaoImpl();
		String token = fbDaoImpl.getFBToken("fb_token", 2);
		Timestamp expireTime = fbDaoImpl.getExpireTime("fb_token", 2);
		int length = fbDaoImpl.getTableLength("fb_token");
		logger.info(token);
		logger.info("time=" + expireTime.getTime());
		logger.info(String.valueOf(length));
	}
}
