package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;

@QuarkusTest
public class PurchaseResourceTests extends GraphQLTest
{
	@Test
	public void testMakePurchase()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/MakePurchase.graphql").replace("DB_PRODUCT_ID", peitsche.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.makePurchase[0].paidPrice", is(6000))
			.body("data.makePurchase[0].account.name", is("Dieter Dubinsky"))
			.body("data.makePurchase[0].product.name.", is("Die Peitsche des Mönchs"));
	}

	@Test
	public void testRefundYoyo()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(even.getId()))
			.body(getGraphQLBody("graphql/Refund.graphql").replace("DB_ID", purchase3.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.refundPurchase", is(true));
	}

	@Test
	public void testExpiredRefundPeitsche()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/Refund.graphql").replace("DB_ID", expiredPurchase.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("refund-period-exceeded"))
			.body("errors[0].extensions.exception",
				is("de.keyruu.nexcalimat.graphql.exception.RefundPeriodExceededException"));
	}

	@Test
	public void testRefundForWrongPerson()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/Refund.graphql").replace("DB_ID", purchase2.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("errors[0].extensions.code", is("forbidden"))
			.body("errors[0].extensions.exception",
				is("io.quarkus.security.ForbiddenException"));
	}

	@Test
	public void testGetPurchasesForCustomer()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/GetMyPurchases.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.myPurchases.data.size()", is(2))
			.body("data.myPurchases.page", is(0))
			.body("data.myPurchases.total", is(2))
			.body("data.myPurchases.data[1].account.name", is("Dieter Dubinsky"));
	}

	@Test
	public void testGetPurchasesForUser()
	{
		given()
			.when()
			.auth()
			.oauth2(
				getOidcToken("dubinsky", Set.of("some-random-user-group-name"), "dubinsky@keyruu.de", "Dieter Dubinsky"))
			.body(getGraphQLBody("graphql/GetMyPurchases.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.myPurchases.data.size()", is(2))
			.body("data.myPurchases.page", is(0))
			.body("data.myPurchases.total", is(2))
			.body("data.myPurchases.data[1].account.name", is("Dieter Dubinsky"));
	}

	@Test
	public void testAllGetPurchases()
	{
		given()
			.when()
			.auth()
			.oauth2(
				getOidcToken("earl", Set.of("some-random-admin-group-name"), "earl@keyruu.de", "Earl of Cockwood"))
			.body(getGraphQLBody("graphql/GetAllPurchases.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.purchases.data.size()", is(5));
	}
}
