package zx.soft.snd.web.server;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

import zx.soft.snd.twitter.appllication.NameApplication;

/**
 * 社交帐号服务类
 * @author fgq
 *
 */
public class NameServer {

	private final Component component;
	private final Application nameApplication;
	private final int PORT = 8100;

	public NameServer() {
		component = new Component();
		nameApplication = new NameApplication();
	}

	private void start() {
		component.getServers().add(Protocol.HTTP, PORT);
		component.getDefaultHost().attach("/name", nameApplication);
		try {
			component.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		NameServer nameServer = new NameServer();
		nameServer.start();
	}
}
