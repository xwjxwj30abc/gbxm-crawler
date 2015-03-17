package zx.soft.gbxm.google.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.gbxm.google.domain.UserInfo;
import zx.soft.gbxm.google.utils.MybatisConfig;

public class GoogleDaoImpl {

	private static Logger logger = LoggerFactory.getLogger(GoogleDaoImpl.class);
	private static SqlSessionFactory sqlSessionFactory;

	public GoogleDaoImpl() {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory();
	}

	//获得表长度
	public int getTableCount(String tableName) {
		int count = 0;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			count = googleDao.getTableCount(tableName);
		}
		return count;
	}

	//插入状态信息
	public void insertStatusInfo(List<StatusInfo> statusInfoes) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			for (StatusInfo statusInfo : statusInfoes) {
				try {
					googleDao.insertStatusInfo(statusInfo);
					logger.info("insert  " + statusInfo.toString() + " succeed.");
				} catch (Exception e) {
					try {
						statusInfo.setContent(new String(statusInfo.getContent().getBytes(), "GBK"));
						googleDao.insertStatusInfo(statusInfo);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	//获取待查询的所有用户的id
	public List<String> getUserIdList(String tableName) {
		List<String> userIdList = new ArrayList<>();
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			userIdList = googleDao.getAllUserId(tableName);
		}
		return userIdList;
	}

	//插入用户信息
	public void insertUserInfo(UserInfo userInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			googleDao.insertUserInfo(userInfo);
			logger.info("insert userInfo" + userInfo.toString() + " succeed.");
		}
	}

	//判断指定用户id是否存在
	public boolean isExisted(String tableName, String userId) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			if (googleDao.getNameByUserId(tableName, userId) == null) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
	}

	//根据用户Id获取用户名
	public String getNameByUserId(String tableName, String userId) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			return googleDao.getNameByUserId(tableName, userId);
		}
	}

	//根据用户Id获取上次更新时间
	public Timestamp getLastUpdateTimeByUserId(String tableName, String userId) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			return googleDao.getLastUpdateTimeByUserId(tableName, userId);
		}
	}

	//更新读取用户数据的时间记录
	public void updatedUserInfo(String userId, Timestamp lastUpdateTime) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			googleDao.updateUserInfo(userId, lastUpdateTime);
			logger.info("update userInfo");
		}
	}

}
