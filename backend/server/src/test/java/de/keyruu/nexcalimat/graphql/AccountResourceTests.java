package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import de.keyruu.nexcalimat.model.Account;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.response.ValidatableResponse;

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

  @Test
  public void testGetDeletedAccounts() {
    getDeletedAccountsQuery()
        .body("data.deletedAccounts.size()", is(0));

    deleteAccount(even);

    getDeletedAccountsQuery()
        .body("data.deletedAccounts.size()", is(1))
        .body("data.deletedAccounts[0].deletedAt", is(notNullValue()))
        .body("data.deletedAccounts[0].name", is("Even Longer"));
  }

  @Transactional
  void deleteAccount(Account account) {
    _accountRepository.delete(account);
  }

  private ValidatableResponse getDeletedAccountsQuery() {
    return given()
        .when()
        .auth().oauth2(getOidcToken("dubinsky", Set.of("some-random-admin-group-name"), "dubinsky@keyruu.de", "Doris"))
        .body(getGraphQLBody("graphql/GetDeletedAccounts.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200);
  }
}
