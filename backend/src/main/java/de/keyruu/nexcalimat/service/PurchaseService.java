package de.keyruu.nexcalimat.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.PurchaseNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.RefundPeriodExceededException;
import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.DiscountRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PurchaseService
{
	@Inject
	PurchaseRepository purchaseRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	AccountRepository accountRepository;

	@Inject
	DiscountRepository discountRepository;

	public PaginationResponse<Purchase> listAll(Mapper mapper)
	{
		List<Purchase> purchases = purchaseRepository.findAll(mapper.getSort()).page(mapper.getPage()).list();
		long count = purchaseRepository.count();
		return new PaginationResponse<>(purchases, count, mapper);
	}

	public Purchase findById(Long id)
	{
		return purchaseRepository.findById(id);
	}

	@Transactional
	public List<Purchase> makePurchase(Long productId, Long accountId, Integer amount)
	{
		Product product = productRepository.findByIdOptional(productId).orElseThrow(ProductNotFoundException::new);
		Account account = accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

		List<Purchase> purchases = new ArrayList<>();
		for (int i = 0; i < amount; i++)
		{
			Purchase purchase = new Purchase();
			int price = getPaidPrice(product, account);

			purchase.setAccount(account);
			purchase.setProduct(product);
			purchase.setPaidPrice(price);

			purchaseRepository.persist(purchase);

			account.setBalance(account.getBalance() - price);

			accountRepository.persist(account);

			purchases.add(purchase);
		}

		return purchases;
	}

	@Transactional
	public Boolean refund(Long purchaseId, Long accountId)
	{
		Purchase purchase = purchaseRepository.findByIdOptional(purchaseId).orElseThrow(PurchaseNotFoundException::new);
		Account account = accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

		if (!purchase.getAccount().equals(account))
		{
			throw new ForbiddenException();
		}

		if (LocalDateTime.now().isAfter(purchase.getCreatedAt().plusMinutes(5)))
		{
			throw new RefundPeriodExceededException();
		}

		purchase.setDeletedAt(LocalDateTime.now());

		purchaseRepository.persist(purchase);

		account.setBalance(account.getBalance() + purchase.getPaidPrice());

		accountRepository.persist(account);

		return Boolean.TRUE;
	}

	public PaginationResponse<Purchase> getPurchasesForUser(String extId, Mapper mapper)
	{
		Account account = accountRepository.find("extId", extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		return getPurchasesForAccount(account, mapper);
	}

	public PaginationResponse<Purchase> getPurchasesForCustomer(Long pinJwtAccountId, Mapper mapper)
	{
		Account account = accountRepository.findByIdOptional(pinJwtAccountId)
			.orElseThrow(AccountNotFoundException::new);

		return getPurchasesForAccount(account, mapper);
	}

	private int getPaidPrice(Product product, Account account)
	{
		int productPrice = product.getPrice();
		if (Boolean.FALSE.equals(account.getDiscounted()))
		{
			return productPrice;
		}

		return BigDecimal.valueOf(productPrice)
			.multiply(BigDecimal.valueOf(((double)100 - discountRepository.getDiscount()) / 100))
			.setScale(0, RoundingMode.HALF_UP)
			.intValue();
	}

	private PaginationResponse<Purchase> getPurchasesForAccount(Account account, Mapper mapper)
	{
		String query = "account = ?1 AND deletedAt IS NULL";
		List<Purchase> purchases = purchaseRepository.find(query, mapper.getSort(), account).page(mapper.getPage()).list();
		long count = purchaseRepository.count(query, account);
		return new PaginationResponse<>(purchases, count, mapper);
	}
}
