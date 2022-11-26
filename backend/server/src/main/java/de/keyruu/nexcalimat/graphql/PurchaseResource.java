package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.ProductService;
import de.keyruu.nexcalimat.service.PurchaseService;

@GraphQLApi
public class PurchaseResource {
  @Inject
  PurchaseRepository _purchaseRepository;

  @Inject
  PurchaseService _purchaseService;

  @Inject
  JwtUtils _jwtUtils;

  @Query
  @Description("Get all Purchases")
  @RolesAllowed({ Roles.ADMIN })
  public List<Purchase> purchases() {
    return _purchaseRepository.listAll();
  }

  @Query
  @Description("Get Purchase by ID")
  @RolesAllowed({ Roles.ADMIN })
  public Purchase purchase(Long id) {
    return _purchaseRepository.findById(id);
  }

  @Mutation
  @Description("Make Purchase")
  @RolesAllowed(Roles.CUSTOMER)
  public Purchase makePurchase(Long productId) {
    return _purchaseService.makePurchase(productId, _jwtUtils.getPinJwtAccountId());
  }

  @Mutation
  @Description("Refund Purchase")
  @RolesAllowed(Roles.CUSTOMER)
  @Transactional
  public Boolean refund(Long id) {
    // TODO
    return null;
  }
}
