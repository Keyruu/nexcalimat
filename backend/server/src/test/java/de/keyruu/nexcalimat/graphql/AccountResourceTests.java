package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AccountResourceTests extends GraphQLTest {
  @Test
  public void testSignUp() {
    given()
        .when()
        .auth().oauth2(getOidcToken("rather", Set.of("some-random-user-group-name"), "rather@short.de", "Rather Short"))
        .body(getGraphQLBody("graphql/SignUp.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(startsWith(
            "{\"data\":{\"signUp\":{\"email\":\"rather@short.de\",\"extId\":\"rather\",\"name\":\"Rather Short\",\"balance\":0,"));
  }

  @Test
  public void testAlreadyExisitingSignUp() {
    given()
        .when()
        .auth().oauth2(getOidcToken("dubinsky", Set.of("some-random-user-group-name"), "dubinsky@keyruu.de", "Doris"))
        .body(getGraphQLBody("graphql/SignUp.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(containsString("\"code\":\"account-exists\""));
  }

  @Test
  public void testPinLogin() {
    given()
        .when()
        .body(getGraphQLBody("graphql/PinLogin.graphql").replace("DB_ID", dubinsky.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(startsWith("{\"data\":{\"pinLogin\":\"ey"));
  }

  @Test
  public void testGetAccounts() {
    given()
        .when()
        .body(getGraphQLBody("graphql/GetAccounts.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"accounts\":[{\"name\":\"Dieter Dubinsky\",\"email\":\"dubinsky@keyruu.de\",\"balance\":0,\"extId\":\"dubinsky\"},{\"name\":\"Even Longer\",\"email\":\"even@keyruu.de\",\"balance\":0,\"extId\":\"even\"}]}}"));
  }
}
