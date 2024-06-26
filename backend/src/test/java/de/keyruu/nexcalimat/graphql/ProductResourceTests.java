package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import jakarta.ws.rs.core.HttpHeaders;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
class ProductResourceTests extends GraphQLTest
{
	@Test
	void testGetProductsUnauthorized()
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
	void testGetProducts()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/GetProducts.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.products.data.size()", is(3))
			.body("data.products.page", is(0))
			.body("data.products.total", is(3))
			.body("data.products.data[0].name", is("Die Peitsche des Mönchs"))
			.body("data.products.data[0].price", is(6000))
			.body("data.products.data[1].name", is("Das Yoyo von Long"))
			.body("data.products.data[1].price", is(80));
	}

	@Test
	void testGetProductsWithSort()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/GetProductsWithSort.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.products.data.size()", is(3))
			.body("data.products.page", is(0))
			.body("data.products.total", is(3))
			.body("data.products.data[2].name", is("Die Peitsche des Mönchs"))
			.body("data.products.data[2].price", is(6000))
			.body("data.products.data[0].name", is("Das Yoyo von Long"))
			.body("data.products.data[0].price", is(80));
	}

	@Test
	void testGetProductsWithFavorite()
	{
		given()
			.when()
			.header(HttpHeaders.AUTHORIZATION, "PIN " + getPinToken(dubinsky.getId()))
			.body(getGraphQLBody("graphql/GetProductsWithFavorite.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.productsWithFavorites.data.size()", is(3))
			.body("data.productsWithFavorites.page", is(0))
			.body("data.productsWithFavorites.total", is(3))
			.body("data.productsWithFavorites.data[2].name", is("Die Peitsche des Mönchs"))
			.body("data.productsWithFavorites.data[2].price", is(6000))
			.body("data.productsWithFavorites.data[0].name", is("Die Maske des Wixxers"))
			.body("data.productsWithFavorites.data[0].price", is(66));
	}

	@Test
	void testGetPeitsche()
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
	void testUpdatePeitsche()
	{
		given()
			.when()
			.auth()
			.oauth2(getOidcToken("rather", Set.of("some-random-admin-group-name"),
				"rather@short.de",
				"Rather Short"))
			.body(getGraphQLBody("graphql/UpdateProduct.graphql").replace("DB_ID", peitsche.getId().toString()))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body("data.updateProduct.name", is("Die noch größere Peitsche des Mönchs"))
			.body("data.updateProduct.price", is(6000))
			.body("data.updateProduct.id", is(peitsche.getId().intValue()));
	}
}
