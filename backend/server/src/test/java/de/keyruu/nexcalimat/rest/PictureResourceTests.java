package de.keyruu.nexcalimat.rest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.junit.jupiter.api.Test;

import de.keyruu.nexcalimat.NexcalimatTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class PictureResourceTests extends NexcalimatTest
{
  @Test
  public void updateAccountPicture()
  {
    given()
      .when()
      .auth().oauth2(getEarlsToken())
      .multiPart("filename", "rather.png")
      .multiPart("file", new File("src/test/resources/rather.png"))
      .post("/api/v1/picture/account/" + even.getId())
      .then()
      .statusCode(201);
  }

  @Test
  public void updateMyAccountPicture()
  {
    given()
      .when()
      .auth().oauth2(getDubinskysToken())
      .multiPart("filename", "rather.png")
      .multiPart("file", new File("src/test/resources/rather.png"))
      .post("/api/v1/picture/myAccount")
      .then()
      .statusCode(201);
  }

  @Test
  public void updateOtherAccountPictureAsUser()
  {
    given()
      .when()
      .auth().oauth2(getDubinskysToken())
      .multiPart("filename", "rather.png")
      .multiPart("file", new File("src/test/resources/rather.png"))
      .post("/api/v1/picture/account/" + even.getId())
      .then()
      .statusCode(403);
  }

  @Test
  public void updateProductPicture()
  {
    given()
      .when()
      .auth().oauth2(getEarlsToken())
      .multiPart("filename", "peitsche.png")
      .multiPart("file", new File("src/test/resources/peitsche.png"))
      .post("/api/v1/picture/product/" + peitsche.getId())
      .then()
      .statusCode(201);
  }
}
