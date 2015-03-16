package zx.soft.gbxm.google.dao;

import java.io.UnsupportedEncodingException;
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
			if (googleDao.getIdByUserId(tableName, userId) == null) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	//更新用户信息
	public void updatedUserInfo(UserInfo userInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			GoogleDao googleDao = sqlSession.getMapper(GoogleDao.class);
			googleDao.updateUserInfo(userInfo);
		}
	}

}
