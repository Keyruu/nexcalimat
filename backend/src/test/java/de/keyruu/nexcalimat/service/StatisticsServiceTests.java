package de.keyruu.nexcalimat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.keyruu.nexcalimat.NexcalimatTest;
import de.keyruu.nexcalimat.model.projection.ProductPurchaseCount;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class StatisticsServiceTests extends NexcalimatTest
{
	@Inject
	StatisticsService statisticsService;

	@Test
	void testProductPurchaseCountPeitsche()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProduct(peitsche.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Peitsche des Mönchs", purchaseCountForProduct.getProduct().getName());
		assertEquals(2, purchaseCountForProduct.getCount());
	}

	@Test
	void testProductPurchaseCountPeitscheLastMonth()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProductLastMonth(peitsche.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Peitsche des Mönchs", purchaseCountForProduct.getProduct().getName());
		assertEquals(2, purchaseCountForProduct.getCount());
		assertEquals(1, purchaseCountForProduct.getRecommendedPurchaseAmount());
	}

	@Test
	void testProductPurchaseCountPeitscheTimeRange()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProduct(peitsche.getId(), LocalDateTime.of(2022, 11, 11, 0, 0), LocalDateTime.of(2022, 12, 12, 0, 0));

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Peitsche des Mönchs", purchaseCountForProduct.getProduct().getName());
		assertEquals(0, purchaseCountForProduct.getCount());
	}

	@Test
	void testProductPurchaseCountYoyo()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProduct(yoyo.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Das Yoyo von Long", purchaseCountForProduct.getProduct().getName());
		assertEquals(2, purchaseCountForProduct.getCount());
	}

	@Test
	void testProductPurchaseCountInvalidProductId()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProduct(333L);

		assertNotNull(purchaseCountForProduct);
		assertNull(purchaseCountForProduct.getProduct());
		assertEquals(0, purchaseCountForProduct.getCount());
	}

	@Test
	void testProductPurchaseCountProductWithoutPurchases()
	{
		ProductPurchaseCount purchaseCountForProduct = statisticsService.getPurchaseCountForProduct(maske.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Maske des Wixxers", purchaseCountForProduct.getProduct().getName());
		assertEquals(0, purchaseCountForProduct.getCount());
	}

	@Test
	void testPurchaseCountForAllBoughtProductsLastMonth()
	{
		List<ProductPurchaseCount> purchaseCounts = statisticsService.getPurchaseCountForAllBoughtProductsLastMonth();

		assertNotNull(purchaseCounts);
	}
}
