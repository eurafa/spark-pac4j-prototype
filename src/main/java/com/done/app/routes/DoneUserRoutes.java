package com.done.app.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DoneUserRoutes extends DoneRoutes {

	public static ModelAndView dashboard(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "dashboard.mustache");
	}

	public static ModelAndView profile(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "profile.mustache");
	}

	public static ModelAndView payments(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "payments.mustache");
	}

	public static ModelAndView calendar(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "calendar.mustache");
	}

	public static ModelAndView finances(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "finances.mustache");
	}

	public static ModelAndView students(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "students.mustache");
	}

	public static ModelAndView student(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "student.mustache");
	}

	public static ModelAndView groups(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "groups.mustache");
	}

	public static ModelAndView group(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "group.mustache");
	}

	public static ModelAndView exam(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "exam.mustache");
	}

	public static ModelAndView programs(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "programs.mustache");
	}

	public static ModelAndView program(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "program.mustache");
	}

	public static ModelAndView trainings(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "trainings.mustache");
	}

	public static ModelAndView training(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "training.mustache");
	}

	public static ModelAndView exercises(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "exercises.mustache");
	}

	public static ModelAndView exercise(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "exercise.mustache");
	}

	public static ModelAndView contracts(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "contracts.mustache");
	}

	public static ModelAndView upgrade(final Request request, final Response response) {
		return new ModelAndView(mapWithProfile(request, response), "upgrade.mustache");
	}

}
