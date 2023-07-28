package de.keyruu.nexcalimat.service;

import java.util.Optional;

import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Favorite;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.FavoriteRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FavoriteService
{
	@Inject
	FavoriteRepository _favoriteRepository;

	@Inject
	ProductRepository _productRepository;

	@Inject
	AccountRepository _accountRepository;

	@Transactional
	public Boolean toggleFavorite(Long productId, Long accountId)
	{
		Product product = _productRepository.findByIdOptional(productId).orElseThrow(ProductNotFoundException::new);
		Account account = _accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

		Optional<Favorite> favorite = _favoriteRepository
			.find("product.id = ?1 AND account.id = ?2", productId, accountId)
			.firstResultOptional();

		if (favorite.isPresent())
		{
			_favoriteRepository.delete(favorite.get());
			return false;
		}
		else
		{
			Favorite newFavorite = new Favorite();
			newFavorite.setAccount(account);
			newFavorite.setProduct(product);
			_favoriteRepository.persist(newFavorite);
			return true;
		}
	}
}
