package de.keyruu.nexcalimat.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import de.keyruu.nexcalimat.service.ProductService;
import de.keyruu.nexcalimat.service.PurchaseService;
import de.keyruu.nexcalimat.utils.TestUtils;
import io.smallrye.jwt.build.Jwt;

public class GraphQLTest {
  Account dubinsky = TestUtils.dubinsky();
  Account hai = TestUtils.hai();
  Account even = TestUtils.even();
  Product peitsche = TestUtils.peitsche();
  Product yoyo = TestUtils.yoyo();
  Purchase purchase1 = TestUtils.purchase(dubinsky, peitsche, 6000);
  Purchase purchase2 = TestUtils.purchase(even, peitsche, 5000);
  Purchase purchase3 = TestUtils.purchase(even, yoyo, 80);

  @Inject
  AccountService _accountService;

  @Inject
  AccountRepository _accountRepository;

  @Inject
  ProductService _productService;

  @Inject
  ProductRepository _productRepository;

  @Inject
  PurchaseService _purchaseService;

  @Inject
  PurchaseRepository _purchaseRepository;

  @BeforeEach
  @Transactional
  public void testData() {
    _accountRepository.persist(dubinsky, even, hai);
    _productRepository.persist(peitsche, yoyo);
    _purchaseRepository.persist(purchase1, purchase2, purchase3);
    _accountRepository.delete(hai);
  }

  @AfterEach
  @Transactional
  void delete() {
    _purchaseRepository.deleteAll();
    _accountRepository.deleteAll();
    _productRepository.deleteAll();
  }

  void testOidcGraphQlEndpoint(String idToken, String requestBody, Matcher<String> responseBodyMatcher) {
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

  void testPinGraphQlEndpoint(String pinToken, String requestBody, String expectedResponseBody) {
    given()
        .when()
        .header("Authorization", "PIN" + pinToken)
        .body(requestBody)
        .post("/graphql")
        .then()
        .statusCode(200)
        .body(is(expectedResponseBody));
  }

  String getGraphQLBody(String path) {
    try {
      String graphql = new String(Files.readAllBytes(Paths.get("src/test/resources/" + path)));
      graphql = graphql.replaceAll("\"", "\\\\\"");
      graphql = graphql.replaceAll("\n", "");
      return "{\"query\": \""
          + graphql + "\"}";
    } catch (IOException e) {
      fail("Bad syntax", e);
      return "";
    }
  }

  String getOidcToken(String userName, Set<String> groups, String email, String name) {
    return Jwt.preferredUserName(userName)
        .groups(groups)
        .issuer("https://server.example.com")
        .audience("https://service.example.com")
        .claim("sub", userName)
        .claim("email", email)
        .claim("name", name)
        .sign();
  }

  String getPinToken(Long userId) {
    return Jwt.upn(userId.toString())
        .groups(Roles.CUSTOMER)
        .sign("privateKey.pem");
  }
}
