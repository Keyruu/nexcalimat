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
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.PurchaseService;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
	public PaginationResponse<Purchase> purchases(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return _purchaseService.listAll(Mapper.map(page, sort));
	}

	@Query
	@Description("Get personal Purchases")
	@RolesAllowed({ Roles.CUSTOMER, Roles.USER })
	public PaginationResponse<Purchase> myPurchases(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		Mapper mapper = Mapper.map(page, sort);
		if (_securityIdentity.hasRole(Roles.CUSTOMER))
		{
			return _purchaseService.getPurchasesForCustomer(_jwtUtils.getPinJwtAccountId(_request), mapper);
		}
		else
		{
			return _purchaseService.getPurchasesForUser(_jwtUtils.getExtIdFromToken(_jwt), mapper);
		}
	}

	@Query
	@Description("Get Purchase by ID")
	@RolesAllowed(Roles.ADMIN)
	public Purchase purchase(Long id)
	{
		return _purchaseService.findById(id);
	}

	@Mutation
	@Description("Make Purchase")
	@RolesAllowed(Roles.CUSTOMER)
	public List<Purchase> makePurchase(Long productId, Integer amount)
	{
		return _purchaseService.makePurchase(productId, _jwtUtils.getPinJwtAccountId(_request), amount);
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
