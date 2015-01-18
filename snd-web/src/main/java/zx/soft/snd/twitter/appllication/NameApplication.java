package zx.soft.snd.twitter.appllication;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import zx.soft.snd.twitter.resource.NameServerResource;
import zx.soft.snd.web.dao.DaoImpl;
import zx.soft.snd.web.domain.PostData;

public class NameApplication extends Application {

	private DaoImpl daoImpl;

	public NameApplication() {
		daoImpl = new DaoImpl();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/name", NameServerResource.class);
		return router;
	}

	public void storeName2DB(PostData data) {
		List<String> names = new ArrayList<>();
		names = data.getNameList();
		daoImpl.insertNames(names);
	}
}
