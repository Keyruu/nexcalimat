package de.keyruu.nexcalimat.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.PurchaseNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.RefundPeriodExceededException;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import io.quarkus.security.ForbiddenException;

@ApplicationScoped
public class PurchaseService {
  @Inject
  PurchaseRepository _purchaseRepository;

  @Inject
  ProductRepository _productRepository;

  @Inject
  AccountRepository _accountRepository;

  @Transactional
  public Purchase makePurchase(Long productId, Long accountId) {
    Product product = _productRepository.findByIdOptional(productId).orElseThrow(ProductNotFoundException::new);
    Account account = _accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

    Purchase purchase = new Purchase();

    purchase.setAccount(account);
    purchase.setProduct(product);
    purchase.setPaidPrice(product.getPrice());

    _purchaseRepository.persist(purchase);

    account.setBalance(account.getBalance() - product.getPrice());

    _accountRepository.persist(account);

    return purchase;
  }

  public Boolean refund(Long purchaseId, Long accountId) {
    Purchase purchase = _purchaseRepository.findByIdOptional(purchaseId).orElseThrow(PurchaseNotFoundException::new);
    Account account = _accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

    if (purchase.getAccount().equals(account) == false) {
      throw new ForbiddenException();
    }

    if (LocalDateTime.now().isAfter(purchase.getCreatedAt().plusMinutes(5))) {
      throw new RefundPeriodExceededException();
    }

    _purchaseRepository.delete(purchase);

    account.setBalance(account.getBalance() + purchase.getPaidPrice());

    _accountRepository.persist(account);

    return Boolean.TRUE;
  }

  public List<Purchase> getPurchasesForUser(String extId) {
    Account account = _accountRepository.find("extId", extId).firstResultOptional()
        .orElseThrow(AccountNotFoundException::new);

    return _purchaseRepository.find("account", account).list();
  }

  public List<Purchase> getPurchasesForCustomer(Long pinJwtAccountId) {
    Account account = _accountRepository.findByIdOptional(pinJwtAccountId).orElseThrow(AccountNotFoundException::new);
    return _purchaseRepository.find("account", account).list();
  }
}
