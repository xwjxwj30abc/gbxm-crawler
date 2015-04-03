package zx.soft.gbxm.facebook.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.facebook.domain.FacebookStatus;

public interface FBDao {

	@Select("SELECT  `token` FROM `fb_token` WHERE id=#{id}")
	public String getToken(@Param("id") int id);

	@Select("SELECT  `since` FROM `fb_token` WHERE id=#{id}")
	public long getSince(@Param("id") int id);

	@Select("SELECT max(id) FROM ${tablename}")
	public int getTableLength(@Param("tablename") String tablename);

	@Insert("INSERT `status_info_facebook` (`id`,`from_id`,`from_name`,`message`,`link`,"
			+ "`createdTime`,`updatedTime`,`sharesCount`,`commentsCount`,`likesCount`,`lasttime`) " + "VALUES "
			+ "(#{id},#{from_id},#{from_name},#{message},#{link},#{createdTime},#{updatedTime},#{sharesCount}"
			+ ",#{commentsCount},#{likesCount},now())")
	public void insertFacebookStatus(FacebookStatus facebookStatus);

	@Update("Update `fb_token` SET `since` =#{since} WHERE `token`=#{token}")
	public void updateSince(@Param("since") long since, @Param("token") String token);
}
