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
        .body("data.signUp.email", is("rather@short.de"))
        .body("data.signUp.extId", is("rather"))
        .body("data.signUp.name", is("Rather Short"))
        .body("data.signUp.balance", is(0));
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
        .body("errors[0].extensions.code", is("account-exists"));
  }

  @Test
  public void testPinLogin() {
    given()
        .when()
        .body(getGraphQLBody("graphql/PinLogin.graphql").replace("DB_ID", dubinsky.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body("data.pinLogin", startsWith("ey"));
  }

  @Test
  public void testGetAccounts() {
    given()
        .when()
        .body(getGraphQLBody("graphql/GetAccounts.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body("data.accounts.size()", is(2))
        .body("data.accounts[0].name", is("Dieter Dubinsky"))
        .body("data.accounts[1].name", is("Even Longer"));
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

  @Test
  public void testGetAccount() {
    given()
        .when()
        .body(getGraphQLBody("graphql/GetAccount.graphql").replace("DB_ID", dubinsky.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"account\":{\"name\":\"Dieter Dubinsky\",\"balance\":0,\"email\":\"dubinsky@keyruu.de\",\"extId\":\"dubinsky\"}}}"));
  }

  @Test
  public void testUserDeleteAccount() {
    given()
        .when()
        .auth().oauth2(getOidcToken("earl", Set.of("some-random-user-group-name"), "", "Earl of Cockwood"))
        .body(getGraphQLBody("graphql/DeleteAccount.graphql").replace("DB_ID", even.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(containsString(
            "{\"code\":\"forbidden\",\"exception\":\"io.quarkus.security.ForbiddenException\"}"));
  }

  @Test
  public void testAdminDeleteAccount() {
    given()
        .when()
        .auth().oauth2(getOidcToken("earl", Set.of("some-random-admin-group-name"), "", "Earl of Cockwood"))
        .body(getGraphQLBody("graphql/DeleteAccount.graphql").replace("DB_ID", even.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"deleteAccount\":true}}"));
  }

  @Test
  @Transactional
  public void testDeletedAccount() {
    given()
        .when()
        .auth().oauth2(getOidcToken("earl", Set.of("some-random-admin-group-name"), "", "Earl of Cockwood"))
        .body(getGraphQLBody("graphql/GetDeletedAccounts.graphql"))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"deletedAccounts\":[{\"name\":\"Der dicke Hai\",\"balance\":0,\"email\":\"hai@keyruu.de\",\"extId\":\"hai\"}]}}"));
  }

  @Test
  @Transactional
  public void testEraseAccount() {
    given()
        .when()
        .auth().oauth2(getOidcToken("earl", Set.of("some-random-admin-group-name"), "", "Earl of Cockwood"))
        .body(getGraphQLBody("graphql/EraseAccount.graphql").replace("DB_ID", hai.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"eraseAccount\":true}}"));
  }

  @Test
  @Transactional
  public void testUpdateAccount() {
    given()
        .when()
        .auth().oauth2(getOidcToken("earl", Set.of("some-random-admin-group-name"), "", "Earl of Cockwood"))
        .body(getGraphQLBody("graphql/UpdateAccount.graphql").replace("DB_ID", dubinsky.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"updateAccount\":{\"name\":\"Der Frosch mit Maske\",\"email\":\"frosch@keyruu.de\",\"balance\":1000,\"extId\":\"dubinsky\"}}}"));
  }
}
