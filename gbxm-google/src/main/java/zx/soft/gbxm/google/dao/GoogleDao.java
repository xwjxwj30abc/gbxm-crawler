package zx.soft.gbxm.google.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.google.domain.GoogleToken;
import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.gbxm.google.domain.UserInfo;

public interface GoogleDao {

	@Select("SELECT max(id) FROM ${tableName}")
	public int getTableCount(@Param("tableName") String tableName);

	@Update("UPDATE `googleUserInfos` SET `lastUpdateTime`=#{lastUpdateTime} WHERE `userId`=#{userId}")
	public void updateUserInfo(@Param("userId") String userId, @Param("lastUpdateTime") Timestamp lastUpdateTime);

	@Select("SELECT `id` FROM ${tableName} WHERE `userId`=#{userId}")
	public long getNameByUserId(@Param("tableName") String tableName, @Param("userId") String userId);

	@Select("SELECT `lastUpdateTime` FROM ${tableName} WHERE `userId`=#{userId}")
	public Timestamp getLastUpdateTimeByUserId(@Param("tableName") String tableName, @Param("userId") String userId);

	@Select("SELECT `userId` from ${tableName}")
	public List<String> getAllUserId(@Param("tableName") String tableName);

	@Select("SELECT  `app_name`,`client_id`,`client_secret` FROM ${tableName} WHERE id=#{id}")
	public GoogleToken getGoogleTokenById(@Param("tableName") String tableName, @Param("id") int id);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void insertUserInfo(UserInfo userInfo);

}
