package de.keyruu.nextcalimat.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HasuraResponseBody {
  @JsonProperty("X-Hasura-User-ID")
  private String xHasuraUserID;
  @JsonProperty("X-Hasura-Role")
  private String xHasuraRole;
  @JsonProperty("X-Hasura-User-OIDC-ID")
  private String xHasuraUserOidcId;

  public HasuraResponseBody(String xHasuraUserID, String xHasuraRole, String xHasuraUserOidcId) {
    this.xHasuraUserID = xHasuraUserID;
    this.xHasuraRole = xHasuraRole;
    this.xHasuraUserOidcId = xHasuraUserOidcId;
  }
}
