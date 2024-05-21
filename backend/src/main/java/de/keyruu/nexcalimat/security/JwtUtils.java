package de.keyruu.nexcalimat.security;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.PinJwtException;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtUtils
{
	@Inject
	JWTParser parser;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.user-id")
	String userIdClaim;

	public Long getPinJwtAccountId(CurrentVertxRequest request)
	{
		String pinJwt = request.getCurrent().request().getHeader("Authorization").replace("PIN ", "");
		try
		{
			String upn = parser.parse(pinJwt).getClaim("upn");
			return Long.parseLong(upn);
		}
		catch (ParseException | NumberFormatException e)
		{
			throw new PinJwtException();
		}
	}

	public String getExtIdFromToken(JsonWebToken idToken)
	{
		return (String)idToken.claim(userIdClaim).orElseThrow();
	}
}
