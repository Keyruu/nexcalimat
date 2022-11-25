package de.keyruu.nexcalimat;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.security.JwtUtils;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;

@GraphQLApi
public class HelloGraphQLResource {
	@Inject
	SecurityIdentity _securityIdentity;

	@Inject
	JsonWebToken jwt;

	@Inject
	JwtUtils _jwtUtils;

	@Query
	@Description("Say hello")
	@RolesAllowed("tablet")
	public String sayHello() throws ParseException {
		return "Hello " + _jwtUtils.getPinJwtAccountId();
	}

	@Query
	@Description("Say hello")
	public String jwt() {
		return "Hello "
				+ Jwt.upn("tablet").groups("tablet").sign();
	}
}