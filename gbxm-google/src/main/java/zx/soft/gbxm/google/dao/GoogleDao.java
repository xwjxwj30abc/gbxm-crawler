package zx.soft.gbxm.google.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.gbxm.google.domain.UserInfo;

public interface GoogleDao {

	@Select("SELECT max(id) FROM ${tableName}")
	public int getTableCount(String tableName);

	@Update("update googleUserInfos set userId=#{userInfo.userId},userName={userInfo.userName},"
			+ "lastUpdateTime={userInfo.lastUpdateTime} where userId={userInfo.userId}")
	public void updateUserInfo(@Param("UserInfo") UserInfo userInfo);

	@Select("SELECT 'id' FROM ${tableName} WHERE  'userId'=#{userId}")
	public String getIdByUserId(@Param("tableName") String tableName, @Param("userId") String userId);

	@Select("SELECT 'userId' from ${tableName}")
	public List<String> getAllUserId(@Param("tableName") String tableName);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void insertUserInfo(UserInfo userInfo);
}
