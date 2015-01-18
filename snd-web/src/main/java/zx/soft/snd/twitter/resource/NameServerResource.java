package zx.soft.snd.twitter.resource;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import zx.soft.snd.twitter.appllication.NameApplication;
import zx.soft.snd.web.domain.PostData;

public class NameServerResource extends ServerResource {

	private NameApplication nameApplication;

	@Override
	public void doInit() {
		nameApplication = new NameApplication();
	}

	@Post("json")
	public Object storeName2DB(final PostData data) {
		nameApplication.storeName2DB(data);
		return "ok";
	}

}
