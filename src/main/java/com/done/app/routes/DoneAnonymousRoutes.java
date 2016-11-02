package com.done.app.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DoneAnonymousRoutes extends DoneRoutes {

	public static ModelAndView index(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "index.mustache");
	}

	public static ModelAndView register(final Request request, final Response response) {
		return new ModelAndView(null, "register.mustache");
	}

	public static ModelAndView forgot(final Request request, final Response response) {
		return new ModelAndView(null, "forgot.mustache");
	}

}
