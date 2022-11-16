package de.keyruu.nexcalimat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;

@ApplicationScoped
public class JwtUtils
{
	@Inject
	JWTParser _parser;

	@Inject
	CurrentVertxRequest request;

	String getPinJwtUser() throws ParseException
	{
		String pinJwt = request.getCurrent().request().getHeader("Authorization").replace("PIN ", "");
		return _parser.parse(pinJwt).getClaim("upn");
	}
}
