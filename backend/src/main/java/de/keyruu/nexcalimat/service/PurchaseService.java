package de.keyruu.nexcalimat.service;

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

			purchase.setAccount(account);
			purchase.setProduct(product);
			purchase.setPaidPrice(product.getPrice());

			purchaseRepository.persist(purchase);

			account.setBalance(account.getBalance() - product.getPrice());

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

	private PaginationResponse<Purchase> getPurchasesForAccount(Account account, Mapper mapper)
	{
		String query = "account = ?1 AND deletedAt IS NULL";
		List<Purchase> purchases = purchaseRepository.find(query, mapper.getSort(), account).page(mapper.getPage()).list();
		long count = purchaseRepository.count(query, account);
		return new PaginationResponse<>(purchases, count, mapper);
	}
}
