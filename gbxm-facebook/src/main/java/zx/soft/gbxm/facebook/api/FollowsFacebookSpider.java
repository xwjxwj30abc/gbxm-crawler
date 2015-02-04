package zx.soft.gbxm.facebook.api;

import java.util.Date;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FollowsFacebookSpider {
	public static void main(String[] args) {

		Facebook facebook = new FacebookFactory().getInstance();
		String token = "CAAEyboPYV4UBAJZBI1NZBVqdj47SEZCCw8WaOz054VfRAEKrpAbT4gayEuXL4SErB6XhREzKLaKek84QgmVHeKNiVMB96Kuu7md8s02RNYUgqnr6kEyrhp3xPUyuAZAE2DMaZCUEQ5teStSZAA9AkOrr7eN0ZCjs9kmyXIafYF8e26hLH8yCqwM6DkhN06eaQCTxFEmQKsVxib8iPodiHpf";
		AccessToken accessToken = new AccessToken(token, null);
		facebook.setOAuthAppId("336925216495493", "4d6723811368c72b53ab33a38551dbe1");
		facebook.setOAuthAccessToken(accessToken);
		Reading reading = new Reading();
		reading.limit(123);
		reading.offset(124);
		Date sinceDate = new Date(1_000L);
		reading.since(sinceDate);
		reading.until(new Date());
		int i = 1;
		try {
			ResponseList<Post> feeds = facebook.getHome(reading);
			for (Post feed : feeds) {
				System.out.println(i++ + "  name=" + feed.getFrom().getName() + "; createdTime="
						+ feed.getCreatedTime());
			}
			System.out.println(feeds.size());
		} catch (FacebookException e) {
			e.printStackTrace();
		}
	}
}
