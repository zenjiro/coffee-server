import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
	public static void main(final String[] args) throws Exception {
		final Server server = new Server(8080);
		final ServletContextHandler handler = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		handler.setContextPath("/");
		handler.addServlet(new ServletHolder(new HttpServlet() {
			@Override
			protected void doGet(final HttpServletRequest request,
					final HttpServletResponse response)
					throws ServletException, IOException {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.getWriter().println("こんにちは！");
			}
		}), "/hello");
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
