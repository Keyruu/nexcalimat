package de.keyruu.nexcalimat.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.PinJwtException;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;

@ApplicationScoped
public class JwtUtils
{
	@Inject
	JWTParser _parser;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.user-id")
	String _userIdClaim;

	public Long getPinJwtAccountId(CurrentVertxRequest request)
	{
		String pinJwt = request.getCurrent().request().getHeader("Authorization").replace("PIN ", "");
		try
		{
			String upn = _parser.parse(pinJwt).getClaim("upn");
			return Long.parseLong(upn);
		}
		catch (ParseException | NumberFormatException e)
		{
			throw new PinJwtException();
		}
	}

	public String getExtIdFromToken(JsonWebToken idToken)
	{
		return (String)idToken.claim(_userIdClaim).get();
	}
}
