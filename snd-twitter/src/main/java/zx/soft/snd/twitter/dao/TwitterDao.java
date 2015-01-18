package zx.soft.snd.twitter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.Token;

public interface TwitterDao {
	public static final int PAGECOUNT = 2;

	@Select("SELECT name FROM ${tablename} WHERE id BETWEEN #{from} AND (#{from}+" + (PAGECOUNT - 1) + ")")
	public List<String> getNameList(@Param("tablename") String tablename, @Param("from") int from);

	@Select("SELECT * FROM ${tablename} WHERE id BETWEEN #{from} AND (#{from}+" + PAGECOUNT + ")")
	public List<Token> getTokenList(@Param("tablename") String tablename, @Param("from") int from);

	@Select("SELECT max(id) FROM ${tablename}")
	public int getTableCount(@Param("tablename") String tablename);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void insertName(String name);
}
