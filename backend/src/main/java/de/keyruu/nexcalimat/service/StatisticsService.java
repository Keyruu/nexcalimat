package de.keyruu.nexcalimat.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.projection.AccountPurchaseCount;
import de.keyruu.nexcalimat.model.projection.ProductPurchaseCount;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatisticsService
{
	@Inject
	ProductRepository _productRepository;

	@Inject
	AccountRepository _accountRepository;

	public ProductPurchaseCount getPurchaseCountForProduct(Long productId)
	{
		return getPurchaseCountForProductInternal(productId, null, null);
	}

	public ProductPurchaseCount getPurchaseCountForProductLastMonth(Long productId)
	{
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
		return getPurchaseCountForProduct(productId, oneMonthAgo, today);
	}

	public ProductPurchaseCount getPurchaseCountForProduct(Long productId, LocalDateTime start, LocalDateTime end)
	{
		return getPurchaseCountForProductInternal(productId, start, end);
	}

	public List<ProductPurchaseCount> getPurchaseCountForAllBoughtProductsLastMonth()
	{
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
		return getPurchaseCountForAllBoughtProductsInternal(oneMonthAgo, today);
	}

	public PaginationResponse<AccountPurchaseCount> getLeaderboard(Mapper mapper)
	{
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIDNIGHT);
		String query = """
			SELECT a, COUNT(pu) as count from Account a LEFT JOIN a.purchases pu
			ON pu.deletedAt IS NULL AND pu.createdAt >= :start AND pu.createdAt <= :end
			WHERE a.deletedAt IS NULL
			GROUP BY a
			ORDER BY count DESC
			""";
		long total = getNotDeletedAccountsCount();
		Parameters params = Parameters.with("start", firstDayOfMonth).and("end", today);
		List<AccountPurchaseCount> data = _accountRepository.find(query, params)
			.project(AccountPurchaseCount.class)
			.page(mapper.getPage())
			.list();
		return new PaginationResponse<>(data, total, mapper);
	}

	private ProductPurchaseCount getPurchaseCountForProductInternal(Long productId, LocalDateTime start, LocalDateTime end)
	{
		String query = """
			SELECT p, COUNT(pu) as count from Product p LEFT JOIN p.purchases pu
			ON pu.deletedAt IS NULL
			""";
		Parameters params = Parameters.with("productId", productId);
		if (start != null && end != null)
		{
			query += " AND pu.createdAt >= :start AND pu.createdAt <= :end\n";
			params.and("start", start)
				.and("end", end);
		}
		query += """
			WHERE p.deletedAt IS NULL AND p.id = :productId
			GROUP BY p
			""";

		return _productRepository.find(query, params)
			.project(ProductPurchaseCount.class)
			.firstResultOptional()
			.orElseGet(() -> new ProductPurchaseCount(_productRepository.findById(productId), 0));
	}

	private List<ProductPurchaseCount> getPurchaseCountForAllBoughtProductsInternal(LocalDateTime start, LocalDateTime end)
	{
		String query = """
			SELECT p, COUNT(pu) as count from Product p LEFT JOIN p.purchases pu
			ON pu.deletedAt IS NULL
			""";
		Parameters params = Parameters.with("type", ProductType.COLD_DRINK);
		if (start != null && end != null)
		{
			query += " AND pu.createdAt >= :start AND pu.createdAt <= :end\n";
			params.and("start", start)
				.and("end", end);
		}
		query += """
			WHERE p.deletedAt IS NULL AND p.type = :type
			GROUP BY p
			ORDER BY count DESC
			""";

		return _productRepository.find(query, params)
			.project(ProductPurchaseCount.class)
			.list();
	}

	private long getNotDeletedAccountsCount()
	{
		return _accountRepository.find("SELECT a FROM Account a WHERE a.deletedAt IS NULL").count();
	}
}
