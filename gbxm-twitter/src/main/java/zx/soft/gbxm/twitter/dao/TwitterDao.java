package zx.soft.gbxm.twitter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.twitter.domain.StatusInfo;
import zx.soft.gbxm.twitter.domain.Token;
import zx.soft.gbxm.twitter.domain.UserInfo;

public interface TwitterDao {

	public static final int PAGECOUNT = 100;

	@Select("SELECT name FROM ${tablename} WHERE id BETWEEN #{from} AND (#{from}+" + (PAGECOUNT - 1) + ")")
	public List<String> getNameList(@Param("tablename") String tablename, @Param("from") int from);

	@Select("SELECT * FROM ${tablename} WHERE id=#{id}")
	public Token getToken(@Param("tablename") String tablename, @Param("id") long id);

	@Select("SELECT max(id) FROM ${tablename}")
	public long getTableCount(@Param("tablename") String tablename);

	@Select("SELECT `screenName` FROM ${tablename} WHERE `userId` = #{userId}")
	public String getIdByUserId(@Param("tablename") String tablename, @Param("userId") long userId);

	@Update("update twitterUserInfos set name=#{userInfo.name},location=#{userInfo.location},description=#{userInfo.description},url=#{userInfo.url},"
			+ " followersCount=#{userInfo.followersCount},friendsCount=#{userInfo.friendsCount},favouritesCount=#{userInfo.favouritesCount},"
			+ "listedCount=#{userInfo.listedCount},lastStatusId=#{userInfo.lastStatusId},statusesCount=#{userInfo.statusesCount}  where userId=#{userInfo.userId}")
	public void updateUserInfo(@Param("userInfo") UserInfo userInfo);

	@Update("update twitterTokens set sinceId=#{sinceId}  where id=#{id}")
	public void updateToken(@Param("sinceId") long sinceId, @Param("id") long id);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void insertUserInfo(UserInfo userInfo);

	public void insertName(String name);

	public void insertToken(Token token);

}
