package org.pac4j.demo.spark;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class DoneApp {

	private final static Logger logger = LoggerFactory.getLogger(DoneApp.class);

	public final static MustacheTemplateEngine DONE_DEFAULT_TEMPLATE_ENGINE = new MustacheTemplateEngine();

	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/public"); // Static files

		get("/", DoneApp::index, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/register", DoneApp::register, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/forgot", DoneApp::forgot, DONE_DEFAULT_TEMPLATE_ENGINE);

		final Config config = new DoneConfigFactory().build();
		final CallbackRoute callback = new CallbackRoute(config, null, true);
		get("/callback", callback);
		post("/callback", callback);

		SecurityFilter adminSecurityFilter = new SecurityFilter(config, "FormClient", "admin");
		before("/admin", adminSecurityFilter);
		get("/admin",  DoneApp::admin, DONE_DEFAULT_TEMPLATE_ENGINE);
		
		SecurityFilter userSecurityFilter = new SecurityFilter(config, "FormClient", "user");

		before("/form", userSecurityFilter);
		get("/form", DoneApp::protectedIndex, DONE_DEFAULT_TEMPLATE_ENGINE);
		
		before("/dashboard", userSecurityFilter);
		get("/dashboard", DoneApp::dashboard, DONE_DEFAULT_TEMPLATE_ENGINE);
		
		before("/protected", new SecurityFilter(config, null));
		get("/protected", DoneApp::protectedIndex, DONE_DEFAULT_TEMPLATE_ENGINE);

		get("/loginForm", (rq, rs) -> form(config), DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/logout", new ApplicationLogoutRoute(config, "/"));
		
		
		exception(Exception.class, (e, request, response) -> {
			logger.error("Unexpected exception", e);
			response.body(DONE_DEFAULT_TEMPLATE_ENGINE.render(new ModelAndView(new HashMap<>(), "error500.mustache")));
		});
	}

	private static ModelAndView index(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return new ModelAndView(map, "index.mustache");
	}

	private static ModelAndView register(final Request request, final Response response) {
		return new ModelAndView(null, "register.mustache");
	}

	private static ModelAndView forgot(final Request request, final Response response) {
		return new ModelAndView(null, "forgot.mustache");
	}

	private static ModelAndView form(final Config config) {
		final Map map = new HashMap();
		final FormClient formClient = config.getClients().findClient(FormClient.class);
		map.put("callbackUrl", formClient.getCallbackUrl());
		return new ModelAndView(map, "loginForm.mustache");
	}

	private static ModelAndView protectedIndex(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return new ModelAndView(map, "protectedIndex.mustache");
	}

	private static ModelAndView admin(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return new ModelAndView(map, "admin.mustache");
	}

	private static ModelAndView dashboard(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return new ModelAndView(map, "dashboard.mustache");
	}

	private static List<CommonProfile> getProfiles(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.getAll(true);
	}

}
