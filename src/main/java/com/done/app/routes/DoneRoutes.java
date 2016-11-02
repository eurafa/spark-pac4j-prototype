package com.done.app.routes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;

import spark.Request;
import spark.Response;

public class DoneRoutes {

	static Map mapWithProfile(final Request request, final Response response) {
		final Map map = new HashMap();
		List<CommonProfile> profiles = getProfiles(request, response);
		if (!profiles.isEmpty()) {
			map.put("profile", profiles.get(0));
		}
		return map;
	}

	private static List<CommonProfile> getProfiles(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.getAll(true);
	}
}
