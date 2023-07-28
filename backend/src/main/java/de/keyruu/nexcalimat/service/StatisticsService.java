package de.keyruu.nexcalimat.service;

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
		return _productRepository.find("""
			SELECT p, COUNT(pu) from Product p LEFT JOIN p.purchases pu
			WHERE p.id = :productId AND pu.deletedAt IS NULL
			GROUP BY p
			""", Parameters.with("productId", productId))
			.project(PurchaseCount.class)
			.firstResult();
	}
}
