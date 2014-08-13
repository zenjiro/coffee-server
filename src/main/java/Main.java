import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	static class Count {
		public final int count;

		public Count(final int count) {
			this.count = count;
		}
	}

	public static void main(final String[] args) throws Exception {
		final AtomicInteger count = new AtomicInteger();
		final AtomicLong resetTimeMillis = new AtomicLong(Long.MAX_VALUE);
		final Server server = new Server(8080);
		final HandlerList handlers = new HandlerList();
		final ContextHandler contextHandler = new ContextHandler();
		contextHandler.setContextPath("/webapp");
		final ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase("webapp");
		contextHandler.setHandler(resourceHandler);
		handlers.addHandler(contextHandler);
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
				Logger.getAnonymousLogger().log(
						Level.INFO,
						"count = {0}, resetTime = {1}",
						new Object[] {
								count.get(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
										.format(resetTimeMillis.get()) });
			}

			@Override
			protected void doPost(final HttpServletRequest request,
					final HttpServletResponse response)
					throws ServletException, IOException {
				this.doGet(request, response);
			}
		}), "/mill");
		handler.addServlet(new ServletHolder(new HttpServlet() {
			@Override
			protected void doGet(final HttpServletRequest request,
					final HttpServletResponse response)
					throws ServletException, IOException {
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(),
						new Count(count.get()));
			}
		}), "/status");
		handlers.addHandler(handler);
		server.setHandler(handlers);
		server.start();
		final ScheduledExecutorService resetExecutorService = Executors
				.newSingleThreadScheduledExecutor();
		resetExecutorService.scheduleAtFixedRate(() -> {
			if (System.currentTimeMillis() > resetTimeMillis.get()) {
				count.set(0);
				resetTimeMillis.set(Long.MAX_VALUE);
				Logger.getAnonymousLogger().info("カウンタをリセットしました。");
			}
		}, 1, 1, TimeUnit.SECONDS);
		View.countUpHandler = () -> {
			count.incrementAndGet();
			resetTimeMillis.set(System.currentTimeMillis() + 10_000);
			Logger.getAnonymousLogger().log(
					Level.INFO,
					"count = {0}, resetTime = {1}",
					new Object[] {
							count.get(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
									.format(resetTimeMillis.get()) });
		};
		View.resetHandler = () -> {
			count.set(0);
			resetTimeMillis.set(Long.MAX_VALUE);
			Logger.getAnonymousLogger().info("カウンタをリセットしました。");
		};
		View.closeHandler = () -> {
			resetExecutorService.shutdown();
			try {
				server.stop();
			} catch (final Exception exception) {
				exception.printStackTrace();
			}
		};
		View.main(args);
	}
}
