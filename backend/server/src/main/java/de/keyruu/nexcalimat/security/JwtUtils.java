package de.keyruu.nexcalimat.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.keyruu.nexcalimat.graphql.exception.PinJwtException;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;

@ApplicationScoped
public class JwtUtils {
	@Inject
	JWTParser _parser;

	@Inject
	CurrentVertxRequest _request;

	public Long getPinJwtAccountId() {
		String pinJwt = _request.getCurrent().request().getHeader("Authorization").replace("PIN ", "");
		try {
			String upn = _parser.parse(pinJwt).getClaim("upn");
			return Long.parseLong(upn);
		} catch (ParseException | NumberFormatException e) {
			throw new PinJwtException();
		}
	}
}
