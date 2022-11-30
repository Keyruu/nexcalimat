package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Set;

import javax.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class ProductResourceTests extends GraphQLTest {
    @Test
    public void testGetProductsUnauthorized() {
        given()
                .when()
                .body(getGraphQLBody("graphql/GetProducts.graphql"))
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].extensions.code", is("unauthorized"))
                .body("errors[0].extensions.exception", is("io.quarkus.security.UnauthorizedException"));
    }

    @Test
    public void testGetProducts() {
        given()
                .when()
                .header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
                .body(getGraphQLBody("graphql/GetProducts.graphql"))
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("data.products.size()", is(2))
                .body("data.products[0].name", is("Die Peitsche des Mönchs"))
                .body("data.products[0].price", is(6000))
                .body("data.products[1].name", is("Das Yoyo von Long"))
                .body("data.products[1].price", is(80));
    }

    @Test
    public void testGetPeitsche() {
        given()
                .when()
                .header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
                .body(getGraphQLBody("graphql/GetProductById.graphql").replace("PRODUCT_ID",
                        peitsche.getId().toString()))
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("data.product.name", is("Die Peitsche des Mönchs"))
                .body("data.product.price", is(6000))
                .body("data.product.id", is(peitsche.getId().intValue()));
    }

    @Test
    public void testUpdatePeitsche() {
        given()
                .when()
                .auth().oauth2(getOidcToken("rather", Set.of("some-random-admin-group-name"), "rather@short.de",
                        "Rather Short"))
                .body(setProductInput(getGraphQLBody("graphql/UpdateProduct.graphql")))
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("data.updateProduct.name", is("Die noch größere Peitsche des Mönchs"))
                .body("data.updateProduct.price", is(6000))
                .body("data.updateProduct.id", is(peitsche.getId().intValue()));
    }

    private String setProductInput(String graphQLBody) {
        var productInput = String.format(
                "{id: %d, name: \"Die noch größere Peitsche des Mönchs\", picture: null, price: 6000, bundleSize: 6, type: COLD_DRINK}",
                peitsche.getId());
        productInput = productInput.replaceAll("\"", "\\\\\"");
        return graphQLBody.replace("PRODUCT_INPUT", productInput);
    }
}
