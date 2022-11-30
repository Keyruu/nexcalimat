package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PurchaseResourceTests extends GraphQLTest {
  @Test
  public void testMakePurchase() {
    given()
        .when()
        .header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
        .body(getGraphQLBody("graphql/MakePurchase.graphql").replace("DB_PRODUCT_ID", peitsche.getId().toString()))
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(
            "{\"data\":{\"makePurchase\":{\"paidPrice\":6000,\"account\":{\"name\":\"Dieter Dubinsky\"},\"product\":{\"name\":\"Die Peitsche des Mönchs\"}}}}"));
  }
}
