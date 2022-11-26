package de.keyruu.nexcalimat.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;

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
}
