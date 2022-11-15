package de.keyruu.nextcalimat.auth;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestHeader;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;

@Path("/hasura")
public class HasuraResource {
  @ConfigProperty(name = "oidc.user-id-claim")
  String userIdClaim;

  @ConfigProperty(name = "oidc.role.admin")
  String adminRole;

  @ConfigProperty(name = "oidc.role.user")
  String userRole;

  @ConfigProperty(name = "pin.jwt.secret")
  String pinJwtSecret;

  @Inject
  SecurityIdentity identity;

  @Inject
  JsonWebToken idToken;

  @Inject
  JWTParser jwtParser;

  @GET()
  @Path("hook")
  @Produces(MediaType.APPLICATION_JSON)
  public Response hook(@RestHeader("X-Pin-JWT") String pinJwtString, @Context SecurityContext ctx) {
    String userId = "", role = "", oidcId = "";

    if (hasIdToken()) {
      oidcId = (String) idToken.getClaim(userIdClaim);

      role = getRoleForIdToken(identity);
      if (role == null)
        return Response.status(401).build();
    } else {
      if (pinJwtString == null || pinJwtString.isBlank()) {
        role = "anon";
      } else {
        try {
          JsonWebToken pinJwt = jwtParser.verify(pinJwtString, pinJwtSecret);

          userId = pinJwt.getSubject();
          role = "user";
        } catch (ParseException e) {
          return Response.status(401).build();
        }
      }
    }

    return Response.status(200).entity(new HasuraResponseBody(userId, role, oidcId)).build();
  }

  @GET
  @Path("token")
  @Produces(MediaType.TEXT_PLAIN)
  public String token() {
    return Jwt.issuer("http://localhost:8081/")
        .subject("1")
        .signWithSecret(pinJwtSecret);
  }

  private String getRoleForIdToken(SecurityIdentity securityIdentity) {
    boolean isAdmin = false, isUser = false;
    for (String r : identity.getRoles()) {
      if (r.equals(adminRole)) {
        isAdmin = true;
      } else if (r.equals(userRole)) {
        isUser = true;
      }
    }

    if (isAdmin) {
      return "admin";
    } else if (isUser) {
      return "user";
    } else {
      return null;
    }
  }

  private boolean hasIdToken() {
    return idToken.getClaimNames() != null;
  }
}