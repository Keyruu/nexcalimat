package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AccountResourceTests extends GraphQLTest
{
	@Test
	public void testSignUp()
	{
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
	public void testAlreadyExisitingSignUp()
	{
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
	public void testPinLogin()
	{
		given()
			.when()
			.body(getGraphQLBody("graphql/PinLogin.graphql").replace("DB_ID", dubinsky.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.pinLogin", startsWith("ey"));
	}

	@Test
	public void testWrongPinLogin()
	{
		given()
			.when()
			.body(getGraphQLBody("graphql/PinLogin.graphql")
				.replace("0000", "1111")
				.replace("DB_ID", dubinsky.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("wrong-pin"));
	}

	@Test
	public void testWrongAccountPinLogin()
	{
		given()
			.when()
			.body(getGraphQLBody("graphql/PinLogin.graphql")
				.replace("DB_ID", "90000"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("account-not-found"));
	}

	@Test
	public void testSetPin()
	{
		given()
			.when()
			.auth().oauth2(getOidcToken("dubinsky", Set.of("some-random-user-group-name"), "dubinsky@keyruu.de", "Doris"))
			.body(getGraphQLBody("graphql/SetPin.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.pin", is(true));
	}

	@Test
	public void testWrongAccountSetPin()
	{
		given()
			.when()
			.auth().oauth2(getOidcToken("whoareyou", Set.of("some-random-user-group-name"), "dubinsky@keyruu.de", "Doris"))
			.body(getGraphQLBody("graphql/SetPin.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("account-not-found"));
	}

	@Test
	public void testGetAccounts()
	{
		given()
			.when()
			.body(getGraphQLBody("graphql/GetAccounts.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.accounts.size()", is(2))
			.body("data.accounts[1].name", is("Dieter Dubinsky"))
			.body("data.accounts[0].name", is("Even Longer"));
	}

	@Test
	public void testUserDeleteAccount()
	{
		given()
			.when()
			.auth().oauth2(getOidcToken("earl", Set.of("some-random-user-group-name"), "", "Earl of Cockwood"))
			.body(getGraphQLBody("graphql/DeleteAccount.graphql").replace("DB_ID", even.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("forbidden"))
			.body("errors[0].extensions.exception", is("io.quarkus.security.ForbiddenException"));
	}

	@Test
	public void testAdminDeleteAccount()
	{
		given()
			.when()
			.auth().oauth2(getEarlsToken())
			.body(getGraphQLBody("graphql/DeleteAccount.graphql").replace("DB_ID", even.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.deleteAccount", is(true));
	}

	@Test
	@Transactional
	public void testDeletedAccount()
	{
		given()
			.when()
			.auth().oauth2(getEarlsToken())
			.body(getGraphQLBody("graphql/GetDeletedAccounts.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.deletedAccounts.size()", is(1))
			.body("data.deletedAccounts[0].deletedAt", is(notNullValue()))
			.body("data.deletedAccounts[0].name", is("Der dicke Hai"));
	}

	@Test
	@Transactional
	public void testEraseAccount()
	{
		given()
			.when()
			.auth().oauth2(getEarlsToken())
			.body(getGraphQLBody("graphql/EraseAccount.graphql").replace("DB_ID", hai.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.eraseAccount", is(true));
	}

	@Test
	@Transactional
	public void testUpdateAccount()
	{
		given()
			.when()
			.auth().oauth2(getEarlsToken())
			.body(getGraphQLBody("graphql/UpdateAccount.graphql").replace("DB_ID", dubinsky.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.updateAccount.email", is("frosch@keyruu.de"))
			.body("data.updateAccount.extId", is("dubinsky"))
			.body("data.updateAccount.name", is("Der Frosch mit Maske"))
			.body("data.updateAccount.balance", is(1000));
	}

	@Test
	@Transactional
	public void testUpdateWrongAccount()
	{
		given()
			.when()
			.auth().oauth2(getEarlsToken())
			.body(getGraphQLBody("graphql/UpdateAccount.graphql").replace("DB_ID", "90000"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("account-not-found"));
	}
}
