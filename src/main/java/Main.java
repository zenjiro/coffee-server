import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	public static void main(final String[] args) throws Exception {
		final AtomicInteger count = new AtomicInteger();
		final AtomicLong resetTimeMillis = new AtomicLong(Long.MAX_VALUE);
		final Server server = new Server(8080);
		final ServletContextHandler handler = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		handler.setContextPath("/");
		handler.addServlet(new ServletHolder(new HttpServlet() {
			@Override
			protected void doGet(final HttpServletRequest request,
					final HttpServletResponse response)
					throws ServletException, IOException {
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(),
						new Count(count.incrementAndGet()));
				resetTimeMillis.set(System.currentTimeMillis() + 10_000);
			}
		}), "/mill");
		server.setHandler(handler);
		server.start();
		server.join();
	}

	static class Count {
		public final int count;

		public Count(final int count) {
			this.count = count;
		}
	}
}
