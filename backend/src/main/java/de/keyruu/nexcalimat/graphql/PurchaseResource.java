package de.keyruu.nexcalimat.graphql;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PagePojo;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.graphql.pojo.SortPojo;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.PurchaseService;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

@GraphQLApi
public class PurchaseResource
{
	@Inject
	PurchaseService purchaseService;

	@Inject
	JwtUtils jwtUtils;

	@Inject
	SecurityIdentity securityIdentity;

	@Inject
	CurrentVertxRequest request;

	@Inject
	JsonWebToken jwt;

	@Query
	@Description("Get all Purchases")
	@RolesAllowed(Roles.ADMIN)
	public PaginationResponse<Purchase> purchases(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return purchaseService.listAll(Mapper.map(page, sort));
	}

	@Query
	@Description("Get personal Purchases")
	@RolesAllowed({ Roles.CUSTOMER, Roles.USER })
	public PaginationResponse<Purchase> myPurchases(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		Mapper mapper = Mapper.map(page, sort);
		if (securityIdentity.hasRole(Roles.CUSTOMER))
		{
			return purchaseService.getPurchasesForCustomer(jwtUtils.getPinJwtAccountId(request), mapper);
		}
		else
		{
			return purchaseService.getPurchasesForUser(jwtUtils.getExtIdFromToken(jwt), mapper);
		}
	}

	@Query
	@Description("Get Purchase by ID")
	@RolesAllowed(Roles.ADMIN)
	public Purchase purchase(Long id)
	{
		return purchaseService.findById(id);
	}

	@Mutation
	@Description("Make Purchase")
	@RolesAllowed(Roles.CUSTOMER)
	public List<Purchase> makePurchase(Long productId, Integer amount)
	{
		return purchaseService.makePurchase(productId, jwtUtils.getPinJwtAccountId(request), amount);
	}

	@Mutation
	@Description("Refund Purchase")
	@RolesAllowed(Roles.CUSTOMER)
	public Boolean refundPurchase(Long id)
	{
		return purchaseService.refund(id, jwtUtils.getPinJwtAccountId(request));
	}
}
