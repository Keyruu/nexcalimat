package de.keyruu.nexcalimat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.keyruu.nexcalimat.NexcalimatTest;
import de.keyruu.nexcalimat.model.projection.PurchaseCount;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class StatisticsServiceTests extends NexcalimatTest
{
	@Inject
	StatisticsService _statisticsService;

	@Test
	public void testProductPurchaseCountPeitsche()
	{
		PurchaseCount purchaseCountForProduct = _statisticsService.getPurchaseCountForProduct(peitsche.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Peitsche des Mönchs", purchaseCountForProduct.getProduct().getName());
		assertEquals(2, purchaseCountForProduct.getCount());
	}

	@Test
	public void testProductPurchaseCountYoyo()
	{
		PurchaseCount purchaseCountForProduct = _statisticsService.getPurchaseCountForProduct(yoyo.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Das Yoyo von Long", purchaseCountForProduct.getProduct().getName());
		assertEquals(2, purchaseCountForProduct.getCount());
	}

	@Test
	public void testProductPurchaseCountInvalidProductId()
	{
		PurchaseCount purchaseCountForProduct = _statisticsService.getPurchaseCountForProduct(333L);

		assertNull(purchaseCountForProduct);
	}

	@Test
	public void testProductPurchaseCountProductWithoutPurchases()
	{
		PurchaseCount purchaseCountForProduct = _statisticsService.getPurchaseCountForProduct(maske.getId());

		assertNotNull(purchaseCountForProduct);
		assertEquals("Die Maske des Wixxers", purchaseCountForProduct.getProduct().getName());
		assertEquals(0, purchaseCountForProduct.getCount());
	}
}
