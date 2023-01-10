package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.hamcrest.Matcher;

import de.keyruu.nexcalimat.NexcalimatTest;

public class GraphQLTest extends NexcalimatTest
{

	void testOidcGraphQlEndpoint(String idToken, String requestBody, Matcher<String> responseBodyMatcher)
	{
		given()
			.when()
			.auth().oauth2(idToken)
			.body(getGraphQLBody("graphql/SignUp.graphql"))
			.post("/graphql")
			.then()
			.statusCode(200)
			.body(is(
				"{\"data\":{\"signUp\":{\"email\":\"test@test.de\",\"extId\":\"test\",\"name\":\"Test Klaus\",\"balance\":0,\"id\":1}}}"));
	}

	void testPinGraphQlEndpoint(String pinToken, String requestBody, String expectedResponseBody)
	{
		given()
			.when()
			.header("Authorization", "PIN" + pinToken)
			.body(requestBody)
			.post("/graphql")
			.then()
			.statusCode(200)
			.body(is(expectedResponseBody));
	}

	String getGraphQLBody(String path)
	{
		try
		{
			String graphql = new String(Files.readAllBytes(Paths.get("src/test/resources/" + path)));
			graphql = graphql.replaceAll("\"", "\\\\\"");
			graphql = graphql.replaceAll("\n", "");
			return String.format("""
				{
				  "query": "%s"
				}
				""", graphql);
		}
		catch (IOException e)
		{
			fail("Bad syntax", e);
			return "";
		}
	}
}
