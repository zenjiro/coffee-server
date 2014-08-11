import org.eclipse.jetty.server.Server;

public class Main {
	public static void main(final String[] args) throws Exception {
		final Server server = new Server(8080);
		server.start();
		server.join();
	}
}
