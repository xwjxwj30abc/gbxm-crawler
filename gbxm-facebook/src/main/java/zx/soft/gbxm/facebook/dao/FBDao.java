package zx.soft.gbxm.facebook.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FBDao {

	@Select("SELECT  token FROM ${tablename} WHERE id=#{id}")
	public String getToken(@Param("tablename") String tablename, @Param("id") int id);

	@Select("SELECT  expire_at FROM ${tablename} WHERE id=#{id}")
	public Timestamp getExpireTime(@Param("tablename") String tablename, @Param("id") int id);

	@Select("SELECT max(id) FROM ${tablename}")
	public int getTableLength(@Param("tablename") String tablename);
}
