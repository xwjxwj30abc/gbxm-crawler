package zx.soft.gbxm.google.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.gbxm.google.domain.UserInfo;

public interface GoogleDao {

	@Select("SELECT max(id) FROM ${tableName}")
	public int getTableCount(String tableName);

	@Update("UPDATE `googleUserInfos` SET `lastUpdateTime`=#{lastUpdateTime} WHERE `userId`=#{userId}")
	public void updateUserInfo(@Param("userId") String userId, @Param("lastUpdateTime") Timestamp lastUpdateTime);

	@Select("SELECT `userName` FROM ${tableName} WHERE `userId`=#{userId}")
	public String getNameByUserId(@Param("tableName") String tableName, @Param("userId") String userId);

	@Select("SELECT `lastUpdateTime` FROM ${tableName} WHERE `userId`=#{userId}")
	public Timestamp getLastUpdateTimeByUserId(@Param("tableName") String tableName, @Param("userId") String userId);

	@Select("SELECT `userId` from ${tableName}")
	public List<String> getAllUserId(@Param("tableName") String tableName);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void insertUserInfo(UserInfo userInfo);

}
