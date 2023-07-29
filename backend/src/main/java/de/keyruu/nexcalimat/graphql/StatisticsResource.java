package de.keyruu.nexcalimat.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.model.projection.PurchaseCount;
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
	@Description("Get purchase counts for all bought products of the last month")
	@RolesAllowed(Roles.ADMIN)
	public List<PurchaseCount> purchaseCountsLastMonth()
	{
		return _statisticsService.getPurchaseCountForAllBoughtProductsLastMonth();
	}
}
