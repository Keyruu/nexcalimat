package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
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
                .body(containsString(
                        "{\"code\":\"unauthorized\",\"exception\":\"io.quarkus.security.UnauthorizedException\"}"));
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
                .body(is(
                        "{\"data\":{\"products\":[{\"name\":\"Die Peitsche des Mönchs\",\"price\":6000,\"bundleSize\":6,\"type\":\"COLD_DRINK\"},{\"name\":\"Das Yoyo von Long\",\"price\":80,\"bundleSize\":20,\"type\":\"HOT_DRINK\"}]}}"));
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
                .body(is(
                        "{\"data\":{\"product\":{\"name\":\"Die Peitsche des Mönchs\",\"price\":6000,\"bundleSize\":6,\"type\":\"COLD_DRINK\"}}}"));
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
                .body(is(
                        "{\"data\":{\"updateProduct\":{\"name\":\"Die noch größere Peitsche des Mönchs\",\"price\":6000,\"bundleSize\":6,\"type\":\"COLD_DRINK\"}}}"));
    }

    private String setProductInput(String graphQLBody) {
        var productInput = String.format(
                "{id: %d, name: \"Die noch größere Peitsche des Mönchs\", picture: null, price: 6000, bundleSize: 6, type: COLD_DRINK}",
                peitsche.getId());
        productInput = productInput.replaceAll("\"", "\\\\\"");
        return graphQLBody.replace("PRODUCT_INPUT", productInput);
    }
}
