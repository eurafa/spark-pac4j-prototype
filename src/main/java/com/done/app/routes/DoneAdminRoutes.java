package com.done.app.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DoneAdminRoutes extends DoneRoutes {

	public static ModelAndView dashboard(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-dashboard.mustache");
	}

	public static ModelAndView finances(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-finances.mustache");
	}

	public static ModelAndView exercises(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-exercises.mustache");
	}

	public static ModelAndView licenses(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-licenses.mustache");
	}

	public static ModelAndView users(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-users.mustache");
	}

	public static ModelAndView user(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "admin-user.mustache");
	}

}
