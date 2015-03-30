package zx.soft.gbxm.facebook.api;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;

public class RestFBTest {

	public static void main(String[] args) {
		FacebookClient facebookClient = new DefaultFacebookClient(
				"CAATviZCAcXPoBALjAYeoZAKC8rwkpxHNZCk8uRu5uzPuNBzvZAs1rEWWnFRN2pb7W1FVb4rg67SVx9gcOIEYqa2hp1rW73rxxAQi5OXzxOZBnOuvI3fZCnTrk2rP7edBkaBxYPOXZAwinDQOjMLLrw9YVlZCS4q1OIKzQ6CDRtIZBobvKsmbP7CQY",
				Version.VERSION_2_2);

		Connection<Post> myFeed = facebookClient.fetchConnection("517825994942542/feed", Post.class);//
		System.out.println(myFeed.getData().size());
		//获取指定id用户的基本信息
		JsonObject btaylor = facebookClient.fetchObject("517825994942542", JsonObject.class);
		System.out.println(btaylor);

		System.out.println("********************");
		for (List<Post> myFeedConnectionPage : myFeed)
			for (Post post : myFeedConnectionPage) {
				System.out.println("Post: " + post.getMessage());
				System.out.println("更新时间" + post.getCreatedTime());
				System.out.println(post);
				System.out.println("********************");
			}

	}
}
