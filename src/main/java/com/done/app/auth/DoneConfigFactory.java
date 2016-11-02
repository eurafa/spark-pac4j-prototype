package com.done.app.auth;

import static spark.Spark.halt;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.authorization.authorizer.ProfileAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import com.done.app.DoneApp;

public class DoneConfigFactory implements ConfigFactory {

	@Override
	public Config build() {
		final FormClient client = new FormClient("http://localhost:8080/loginForm", new CustomUsernamePasswordAuthenticator());
		final Clients clients = new Clients("http://localhost:8080/callback", client);
		final Config config = new Config(clients);
		config.setHttpActionAdapter(new DoneHttpActionAdapter());
		config.addAuthorizer("admin", new AdminAuthorizer());
		config.addAuthorizer("user", new RequireAnyRoleAuthorizer("ROLE_PROFESSIONAL", "ROLE_STUDENT"));
		return config;
	}
}

class AdminAuthorizer extends ProfileAuthorizer<CommonProfile> {

    @Override
    public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
        return isAnyAuthorized(context, profiles);
    }

    @Override
    public boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
        if (profile == null) {
            return false;
        }
        return profile.getRoles().contains("ROLE_ADMIN");
    }
}

class DoneHttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Object adapt(int code, SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, DoneApp.DONE_DEFAULT_TEMPLATE_ENGINE.render(new ModelAndView(new HashMap<>(), "error401.mustache")));
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, DoneApp.DONE_DEFAULT_TEMPLATE_ENGINE.render(new ModelAndView(new HashMap<>(), "error403.mustache")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}

class CustomUsernamePasswordAuthenticator implements Authenticator<UsernamePasswordCredentials> {

	protected static final Logger logger = LoggerFactory.getLogger(SimpleTestUsernamePasswordAuthenticator.class);

	@Override
	public void validate(final UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction, CredentialsException {
		if (credentials == null) {
			throwsException("No credential");
		}
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		if (CommonHelper.isBlank(username)) {
			throwsException("Username cannot be blank");
		}
		final CommonProfile profile = new CommonProfile();
		profile.setId(username);
		profile.addAttribute(Pac4jConstants.USERNAME, username);

		if (CommonHelper.areEquals(username, "admin")) {
			profile.addRole("ROLE_ADMIN");
			profile.addAttribute("isAdmin", true);
		}
		if (CommonHelper.areEquals(username, "pro")) {
			profile.addRole("ROLE_PROFESSIONAL");
			profile.addAttribute("isAdmin", false);
		}
		if (CommonHelper.areEquals(username, "both")) {
			profile.addRole("ROLE_PROFESSIONAL");
			profile.addRole("ROLE_STUDENT");
			profile.addAttribute("isAdmin", false);
		}

		credentials.setUserProfile(profile);
	}

	protected void throwsException(final String message)
			throws CredentialsException {
		throw new CredentialsException(message);
	}
}