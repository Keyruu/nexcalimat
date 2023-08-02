package de.keyruu.nexcalimat.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.projection.PurchaseCount;
import de.keyruu.nexcalimat.repository.ProductRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatisticsService
{
	@Inject
	ProductRepository _productRepository;

	public PurchaseCount getPurchaseCountForProduct(Long productId)
	{
		return getPurchaseCountForProductInternal(productId, null, null);
	}

	public PurchaseCount getPurchaseCountForProductLastMonth(Long productId)
	{
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
		return getPurchaseCountForProduct(productId, oneMonthAgo, today);
	}

	public PurchaseCount getPurchaseCountForProduct(Long productId, LocalDateTime start, LocalDateTime end)
	{
		return getPurchaseCountForProductInternal(productId, start, end);
	}

	public List<PurchaseCount> getPurchaseCountForAllBoughtProductsLastMonth()
	{
		LocalDateTime today = LocalDateTime.of(2023, 4, 1, 0, 0);
		LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
		return getPurchaseCountForAllBoughtProductsInternal(oneMonthAgo, today);
	}

	private PurchaseCount getPurchaseCountForProductInternal(Long productId, LocalDateTime start, LocalDateTime end)
	{
		String query = "SELECT p, COUNT(pu) from Product p LEFT OUTER JOIN p.purchases pu WHERE p.id = :productId AND pu.deletedAt IS NULL\n";
		Parameters params = Parameters.with("productId", productId);
		if (start != null && end != null)
		{
			query += "AND pu.createdAt >= :start AND pu.createdAt <= :end\n";
			params.and("start", start)
				.and("end", end);
		}
		query += "GROUP BY p";

		return _productRepository.find(query, params)
			.project(PurchaseCount.class)
			.firstResultOptional()
			.orElseGet(() -> new PurchaseCount(_productRepository.findById(productId), 0));
	}

	private List<PurchaseCount> getPurchaseCountForAllBoughtProductsInternal(LocalDateTime start, LocalDateTime end)
	{
		String query = "SELECT p, COUNT(pu) as count from Product p LEFT OUTER JOIN p.purchases pu WHERE p.type = :type AND pu.deletedAt IS NULL\n";
		Parameters params = Parameters.with("type", ProductType.COLD_DRINK);
		if (start != null && end != null)
		{
			query += "AND pu.createdAt >= :start AND pu.createdAt <= :end\n";
			params.and("start", start)
				.and("end", end);
		}
		query += "GROUP BY p ORDER BY count DESC";

		return _productRepository.find(query, params)
			.project(PurchaseCount.class)
			.list();
	}
}
