package zx.soft.gbxm.twitter.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import zx.soft.gbxm.twitter.domain.Token;
import zx.soft.model.user.TwitterUser;

public interface TwitterDao {

	@Select("SELECT * FROM ${tablename} WHERE id=#{id}")
	public Token getToken(@Param("tablename") String tablename, @Param("id") long id);

	@Select("SELECT max(id) FROM ${tablename}")
	public long getTableCount(@Param("tablename") String tablename);

	@Select("SELECT `name` FROM ${tablename} WHERE `id` = #{id}")
	public String getScreenNameByUserId(@Param("tablename") String tablename, @Param("id") long id);

	@Update("update twitterTokens set sinceId=#{sinceId}  where id=#{id}")
	public void updateToken(@Param("sinceId") long sinceId, @Param("id") long id);

	//插入用户信息
	@Insert("INSERT `user_info_twitter`(`id`,`name`,`screen_name`,`profile_image_url`,`created_at`,"
			+ "`location`,`url`,`favourites_count`,`utc_offset`,`listed_count`,`followers_count`,"
			+ "`lang`,`description`,`verified`,`time_zone`,`statuses_count`,`friends_count`,`lasttime`) "
			+ "VALUES (#{id},#{name},#{screen_name},#{profile_image_url},#{created_at},"
			+ "#{location},#{url},#{favourites_count},#{utc_offset},#{listed_count},#{followers_count},"
			+ "#{lang},#{description},#{verified},#{time_zone},#{statuses_count},#{friends_count},now())")
	public void insertTwitterUser(TwitterUser twitterUser);

	//更新用户信息
	@Update("UPDATE `user_info_twitter` SET  screen_name=#{twitterUser.screen_name},"
			+ "favourites_count=#{twitterUser.favourites_count},listed_count=#{twitterUser.listed_count},"
			+ "followers_count=#{twitterUser.followers_count},description=#{twitterUser.description},"
			+ "verified=#{twitterUser.verified},statuses_count=#{twitterUser.statuses_count},"
			+ "friends_count=#{twitterUser.friends_count}  WHERE id=#{twitterUser.id}")
	public void updateTwitterUser(@Param("twitterUser") TwitterUser twitterUser);

}
