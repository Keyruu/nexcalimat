package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.PurchaseService;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;

@GraphQLApi
public class PurchaseResource
{
  @Inject
  PurchaseRepository _purchaseRepository;

  @Inject
  PurchaseService _purchaseService;

  @Inject
  JwtUtils _jwtUtils;

  @Inject
  SecurityIdentity _securityIdentity;

  @Inject
  CurrentVertxRequest _request;

  @Inject
  JsonWebToken _jwt;

  @Query
  @Description("Get all Purchases")
  @RolesAllowed(Roles.ADMIN)
  public List<Purchase> purchases()
  {
    return _purchaseService.listAll();
  }

  @Query
  @Description("Get personal Purchases")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public List<Purchase> myPurchases()
  {
    if (_securityIdentity.hasRole(Roles.CUSTOMER))
    {
      return _purchaseService.getPurchasesForCustomer(_jwtUtils.getPinJwtAccountId(_request));
    }
    else
    {
      return _purchaseService.getPurchasesForUser(_jwtUtils.getExtIdFromToken(_jwt));
    }
  }

  @Query
  @Description("Get Purchase by ID")
  @RolesAllowed({ Roles.ADMIN })
  public Purchase purchase(Long id)
  {
    return _purchaseService.findById(id);
  }

  @Mutation
  @Description("Make Purchase")
  @RolesAllowed(Roles.CUSTOMER)
  public Purchase makePurchase(Long productId)
  {
    return _purchaseService.makePurchase(productId, _jwtUtils.getPinJwtAccountId(_request));
  }

  @Mutation
  @Description("Refund Purchase")
  @RolesAllowed(Roles.CUSTOMER)
  @Transactional
  public Boolean refundPurchase(Long id)
  {
    return _purchaseService.refund(id, _jwtUtils.getPinJwtAccountId(_request));
  }
}
