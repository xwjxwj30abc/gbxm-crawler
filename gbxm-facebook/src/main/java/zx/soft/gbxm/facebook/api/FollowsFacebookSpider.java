package zx.soft.gbxm.facebook.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.utils.config.ConfigUtil;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;

public class FollowsFacebookSpider {

	public static Logger logger = LoggerFactory.getLogger(FollowsFacebookSpider.class);
	private static Facebook facebook = new FacebookFactory().getInstance();
	private static int offset = 0;
	private static int limit = 100;
	public static int i = 1;

	public FollowsFacebookSpider(AccessToken accessToken) {
		Properties props = ConfigUtil.getProps("app.properties");
		facebook.setOAuthAppId(props.getProperty("appSecret"), props.getProperty("appId"));
		facebook.setOAuthAccessToken(accessToken);
	}

	/**
	 * 获得授权用户的订阅即关注用户的公开状态更新
	 * @return
	 * @throws InterruptedException
	 * @throws FacebookException
	 */
	public List<Post> getHome(Date since, Date until) throws InterruptedException, FacebookException {
		List<Post> posts = new ArrayList<>();
		boolean hasNext = true;
		List<Post> homeLines = new ArrayList<>();
		while (hasNext) {
			logger.info("offset=" + offset);
			homeLines = facebook.getHome(new Reading().limit(limit).offset(offset * limit).since(since).until(until));
			offset++;
			if (homeLines.size() == 0) {
				logger.info("already get all updated Post,next return all  Posts...");
				hasNext = false;
			} else {
				logger.info("add  " + homeLines.size() + " Posts");
				for (Post post : homeLines) {
					logger.info(i++ + "  from: " + post.getFrom().getName());
				}
				posts.addAll(homeLines);
				homeLines.clear();
			}
		}
		offset = 0;
		i = 0;
		return posts;
	}

	public void updateAccessToken(AccessToken accessToken) {
		facebook.setOAuthAccessToken(accessToken);
	}

}
