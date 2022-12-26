package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import java.util.Set;

import javax.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class ProductResourceTests extends GraphQLTest
{
  @Test
  public void testGetProductsUnauthorized()
  {
    given()
      .when()
      .body(getGraphQLBody("graphql/GetProducts.graphql"))
      .post("/graphql")
      .then()
      .statusCode(200)
      .body("errors[0].extensions.code", is("unauthorized"))
      .body("errors[0].extensions.exception",
        is("io.quarkus.security.UnauthorizedException"));
  }

  @Test
  public void testGetProducts()
  {
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
  public void testGetPeitsche()
  {
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
  public void testUpdatePeitschePicture()
  {
    given()
      .when()
      .auth()
      .oauth2(getOidcToken("rather", Set.of("some-random-admin-group-name"),
        "rather@short.de",
        "Rather Short"))
      .body(setProductInputPicture(getGraphQLBody("graphql/UpdateProductPicture.graphql")))
      .post("/graphql")
      .then()
      .statusCode(200)
      .body("data.updateProductPicture.name", is("Die Peitsche des Mönchs"))
      .body("data.updateProductPicture.picture", startsWith("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAfQAAAH0BAMA"));
  }

  @Test
  public void testUpdatePeitsche()
  {
    given()
      .when()
      .auth()
      .oauth2(getOidcToken("rather", Set.of("some-random-admin-group-name"),
        "rather@short.de",
        "Rather Short"))
      .body(setProductInput(getGraphQLBody("graphql/UpdateProduct.graphql")))
      .post("/graphql")
      .then()
      .statusCode(200)
      .body("data.updateProduct.name", is("Die noch größere Peitsche des Mönchs"))
      .body("data.updateProduct.price", is(6000))
      .body("data.updateProduct.id", is(peitsche.getId().intValue()));
  }

  private String setProductInput(String graphQLBody)
  {
    var productInput = String.format(
      "{id: %d, name: \"Die noch größere Peitsche des Mönchs\", picture: null, price: 6000, bundleSize: 6, type: COLD_DRINK}",
      peitsche.getId());
    productInput = productInput.replaceAll("\"", "\\\\\"");
    return graphQLBody.replace("PRODUCT_INPUT", productInput);
  }

  private String setProductInputPicture(String graphQLBody)
  {
    var productInput = String.format(
      "{id: %d, picture: \"data:image/png;base64,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\"}",
      peitsche.getId());
    productInput = productInput.replaceAll("\"", "\\\\\"");
    return graphQLBody.replace("PRODUCT_INPUT", productInput);
  }
}
