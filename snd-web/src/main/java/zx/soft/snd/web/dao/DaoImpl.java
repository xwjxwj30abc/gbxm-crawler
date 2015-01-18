package zx.soft.snd.web.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.snd.web.domain.Token;
import zx.soft.snd.web.utils.MybatisConfig;

public class DaoImpl {

	private Logger logger = LoggerFactory.getLogger(DaoImpl.class);
	private static SqlSessionFactory sqlSessionFactory;

	public DaoImpl() {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory();
	}

	public void insertNames(List<String> names) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Dao dao = session.getMapper(Dao.class);
			for (String name : names) {
				dao.insertName(name);
				logger.info("insert name:" + name + "   ok");
			}
		}
	}

	public void insertToken(List<Token> tokens) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Dao dao = session.getMapper(Dao.class);
			for (Token token : tokens) {
				dao.insertToken(token);
				logger.info("insert tokenkey=" + token.getTokenkey() + " ,tokensecret=" + token.getTokensecret());
			}
		}
	}

}
