package zx.soft.gbxm.facebook.api;

import java.util.Date;

import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;

public class FacebookSpider {

	public static void main(String[] args) throws InterruptedException, FacebookException {

		String str = "CAAEyboPYV4UBAGLjLBOAPd8TeHMJMfkdemAUDWpcL3q0UAbOoHoSA5bvERp3wM2pL7fu5W0DaxbpsaKBOIeVexgPytLlYZACoUh7yWsY8yXySM347lZC2eFKX0UoRSdtS7mDh7VCg5cDyZADU3zB6jZATzGfNJ8OD3BjcBNSUrHNEs0wKEqZCTBCSO6pbrrHPHHZCU9QmSIdveONZAdemwo";
		AccessToken accessToken = new AccessToken(str);
		FollowsFacebookSpider spider = new FollowsFacebookSpider(accessToken);
		int i = 0;
		Date since = new Date(1L);
		Date until = new Date();
		while (true) {
			try {
				until.setTime(System.currentTimeMillis());
				i = spider.getHome(since, until).size();
				System.out.println("posts length=" + i);
				if (i == 0) {
					System.out.println("sleeping...");
					Thread.sleep(30_000L);
				}
				since.setTime(until.getTime());
			} catch (FacebookException e) {
				System.out.println("update accessToken and retry");
				AccessToken newToken = new AccessToken(str);
				spider.updateAccessToken(newToken);
				try {
					i = spider.getHome(since, until).size();
					System.out.println("posts length=" + i);
					if (i == 0) {
						System.out.println("sleeping...");
						Thread.sleep(30_000L);
					}
					since.setTime(until.getTime());
				} catch (FacebookException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
