package com.done.app;

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

import com.done.app.auth.*;
import com.done.app.routes.*;

public class DoneApp {

	private final static Logger logger = LoggerFactory.getLogger(DoneApp.class);

	public final static MustacheTemplateEngine DONE_DEFAULT_TEMPLATE_ENGINE = new MustacheTemplateEngine();

	public static final Config config = new DoneConfigFactory().build();

	public static void main(String[] args) {
		init();

		get("/", DoneAnonymousRoutes::index, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/register", DoneAnonymousRoutes::register, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/forgot", DoneAnonymousRoutes::forgot, DONE_DEFAULT_TEMPLATE_ENGINE);

		SecurityFilter userSecurityFilter = new SecurityFilter(config, "FormClient", "user");
		
		before("/dashboard", userSecurityFilter);
		before("/profile", userSecurityFilter);
		before("/payments", userSecurityFilter);
		before("/calendar", userSecurityFilter);
		before("/finances", userSecurityFilter);
		before("/students", userSecurityFilter);
		before("/student", userSecurityFilter);
		before("/groups", userSecurityFilter);
		before("/group", userSecurityFilter);
		before("/exam", userSecurityFilter);
		before("/programs", userSecurityFilter);
		before("/program", userSecurityFilter);
		before("/trainings", userSecurityFilter);
		before("/training", userSecurityFilter);
		before("/exercises", userSecurityFilter);
		before("/exercise", userSecurityFilter);
		before("/contracts", userSecurityFilter);
		before("/upgrade", userSecurityFilter);
		get("/dashboard", DoneUserRoutes::dashboard, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/profile", DoneUserRoutes::profile, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/payments", DoneUserRoutes::payments, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/calendar", DoneUserRoutes::calendar, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/finances", DoneUserRoutes::finances, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/students", DoneUserRoutes::students, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/student", DoneUserRoutes::student, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/groups", DoneUserRoutes::groups, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/group", DoneUserRoutes::group, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/exam", DoneUserRoutes::exam, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/programs", DoneUserRoutes::programs, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/program", DoneUserRoutes::program, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/trainings", DoneUserRoutes::trainings, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/training", DoneUserRoutes::training, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/exercises", DoneUserRoutes::exercises, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/exercise", DoneUserRoutes::exercise, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/contracts", DoneUserRoutes::contracts, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/upgrade", DoneUserRoutes::upgrade, DONE_DEFAULT_TEMPLATE_ENGINE);
				
		SecurityFilter adminSecurityFilter = new SecurityFilter(config, "FormClient", "admin");
		before("/admin/dashboard", adminSecurityFilter);
		before("/admin/finances", adminSecurityFilter);
		before("/admin/exercises", adminSecurityFilter);
		before("/admin/licenses", adminSecurityFilter);
		before("/admin/users", adminSecurityFilter);
		before("/admin/user", adminSecurityFilter);

		get("/admin/dashboard",  DoneAdminRoutes::dashboard, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/admin/finances",  DoneAdminRoutes::finances, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/admin/exercises",  DoneAdminRoutes::exercises, DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/admin/licenses",  DoneAdminRoutes::licenses, DONE_DEFAULT_TEMPLATE_ENGINE);		
		get("/admin/users",  DoneAdminRoutes::users, DONE_DEFAULT_TEMPLATE_ENGINE);		
		get("/admin/user",  DoneAdminRoutes::user, DONE_DEFAULT_TEMPLATE_ENGINE);		
	}

	private static void init() {
		staticFiles.location("/public");
		port(8080);

		final CallbackRoute callback = new CallbackRoute(config, null, true);
		get("/callback", callback);
		post("/callback", callback);

		exception(Exception.class, (e, request, response) -> {
			logger.error("Unexpected exception", e);
			response.body(DONE_DEFAULT_TEMPLATE_ENGINE.render(new ModelAndView(new HashMap<>(), "error500.mustache")));
		});

		get("/loginForm", (rq, rs) -> form(config), DONE_DEFAULT_TEMPLATE_ENGINE);
		get("/logout", new ApplicationLogoutRoute(config, "/"));
	}

	private static ModelAndView form(final Config config) {
		final Map map = new HashMap();
		final FormClient formClient = config.getClients().findClient(FormClient.class);
		map.put("callbackUrl", formClient.getCallbackUrl());
		return new ModelAndView(map, "loginForm.mustache");
	}
}
