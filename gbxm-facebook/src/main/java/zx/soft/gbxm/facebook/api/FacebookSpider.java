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
					"CAATviZCAcXPoBALjAYeoZAKC8rwkpxHNZCk8uRu5uzPuNBzvZAs1rEWWnFRN2pb7W1FVb4rg67SVx9gcOIEYqa2hp1rW73rxxAQi5OXzxOZBnOuvI3fZCnTrk2rP7edBkaBxYPOXZAwinDQOjMLLrw9YVlZCS4q1OIKzQ6CDRtIZBobvKsmbP7CQY",
					"xwj_zxsoft");
			Integer limit = 200;
			Integer offset = 0;
			Long since = 1L;
			Long until = System.currentTimeMillis();
			boolean flag = true;
			while (flag) {
				PagedList<Post> tests = facebook.feedOperations().getHomeFeed(
						new PagingParameters(limit, limit * offset, since, until));
				System.out.println(tests.size());
				for (Post test : tests) {
					System.out.println("************下个状态*******************");
					System.out.println("状态Id ：" + test.getFrom().getId());
					System.out.println("状态信息 ：" + test.getMessage());
					System.out.println("创建时间 ：" + test.getCreatedTime());
					System.out.println("***************************************");
				}
				offset++;
			}
		} catch (InvalidAuthorizationException e) {
			e.printStackTrace();
		}
	}
}
