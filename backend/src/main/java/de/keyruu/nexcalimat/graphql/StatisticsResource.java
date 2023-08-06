package de.keyruu.nexcalimat.graphql;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PagePojo;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.model.projection.AccountPurchaseCount;
import de.keyruu.nexcalimat.model.projection.ProductPurchaseCount;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.StatisticsService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

@GraphQLApi
public class StatisticsResource
{
	@Inject
	StatisticsService _statisticsService;

	@Query
	@Description("Get purchase counts for all bought products of the lasPt month")
	@RolesAllowed(Roles.ADMIN)
	public List<ProductPurchaseCount> purchaseCountsLastMonth()
	{
		return _statisticsService.getPurchaseCountForAllBoughtProductsLastMonth();
	}

	@Query
	@Description("Get account purchase counts for the last month aka the leaderboard")
	public PaginationResponse<AccountPurchaseCount> leaderboard(Optional<PagePojo> page)
	{
		return _statisticsService.getLeaderboard(Mapper.map(page, Optional.empty()));
	}
}
