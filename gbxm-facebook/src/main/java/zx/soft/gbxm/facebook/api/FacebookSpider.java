package zx.soft.gbxm.facebook.api;

import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class FacebookSpider {
	public static void main(String[] args) {
		int i = 1;
		try {
			FacebookTemplate facebook = new FacebookTemplate(
					"CAAEyboPYV4UBAIQy4MwMiWmHTM0NWYTEGoTZAavB1hxoxIJoBkLlPK6ZCZCTlC6Bq8gibj5Atf0pAcoW9GnPbHhxZCmpxhhS1llTht5kPUZCqnDqVpdcbX0T7J4jigNs8bn2oJpuwV3J4qZBjNQHNVqtdBTqZAIEUoOLgYliJ8cVfWiYaTXSPwsf4JdZArsj9TRqA7mlNRZCdFcGHjoQZCX89X",
					"xwj_zxsoft");
			Integer limit = 200;
			Integer offset = 0;
			Long since = 1L;
			Long until = System.currentTimeMillis();
			boolean flag = true;
			while (flag) {
				PagedList<Post> tests = facebook.feedOperations().getHomeFeed(
						new PagingParameters(limit, offset * limit, since, until));
				System.out.println(tests.size());
				for (Post test : tests) {
					System.out.println(test.getFrom().getId() + test.getMessage());
				}
				if (tests.size() == 0) {
					flag = false;
				}
				offset++;
			}
		} catch (InvalidAuthorizationException e) {
			e.printStackTrace();
		}
	}
}
