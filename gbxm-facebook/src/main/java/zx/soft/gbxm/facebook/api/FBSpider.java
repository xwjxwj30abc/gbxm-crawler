package zx.soft.gbxm.facebook.api;

import java.sql.Date;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.facebook.dao.FBDaoImpl;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;

public class FBSpider {

	private static Logger logger = LoggerFactory.getLogger(FBSpider.class);

	public static void main(String[] args) throws InterruptedException {
		int i = 3;
		FBDaoImpl fbDaoImpl = new FBDaoImpl();
		String token = fbDaoImpl.getFBToken("fb_token", i++);
		AccessToken accessToken = new AccessToken(token, null);
		FollowsFacebookSpider followsFBSpider = new FollowsFacebookSpider(accessToken);
		Date since = new Date(0L);
		Date until = new Date(System.currentTimeMillis());
		while (true) {
			try {
				followsFBSpider.getHome(since, until);
				logger.info("use unchanged token");
				since = until;
				until.setTime(System.currentTimeMillis());
			} catch (FacebookException e) {
				Timestamp expireTime = fbDaoImpl.getExpireTime("fb_token", i);
				logger.info(expireTime.getTime() + "expireTime");
				if (expireTime.getTime() > System.currentTimeMillis()) {
					token = fbDaoImpl.getFBToken("fb_token", i);
					accessToken = new AccessToken(token, null);
					followsFBSpider.updateAccessToken(accessToken);
					try {
						followsFBSpider.getHome(since, until);
						logger.info("use updated token");
						since = until;
						until.setTime(System.currentTimeMillis());
					} catch (FacebookException e1) {
						logger.error(e.getErrorMessage());
					}
				} else {
					if (i == fbDaoImpl.getTableLength("fb_token")) {
						i = 0;
					}
					i++;
				}
			}
		}
	}
}
