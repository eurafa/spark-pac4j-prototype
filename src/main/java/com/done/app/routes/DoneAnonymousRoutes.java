package com.done.app.routes;

import java.util.List;
import org.pac4j.core.profile.CommonProfile;

import spark.ModelAndView;
import spark.Request;
import spark.Response;


public class DoneAnonymousRoutes extends DoneRoutes {

	public static ModelAndView index(final Request request, final Response response) {
		List<CommonProfile> profiles = getProfiles(request, response);
		if (profiles.isEmpty()) {
			return new ModelAndView(mapWithProfile(request, response), "index.mustache");
		} else {
			Boolean isAdmin = (Boolean) profiles.get(0).getAttribute("isAdmin");
			if (isAdmin) {
				response.redirect("/admin/dashboard");
			} else {
				response.redirect("/dashboard");
			}
			return null;
		}
	}

	public static ModelAndView register(final Request request, final Response response) {
		return new ModelAndView(null, "register.mustache");
	}

	public static ModelAndView forgot(final Request request, final Response response) {
		return new ModelAndView(null, "forgot.mustache");
	}

}
