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
	FavoriteRepository favoriteRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	AccountRepository accountRepository;

	@Transactional
	public Boolean toggleFavorite(Long productId, Long accountId)
	{
		Product product = productRepository.findByIdOptional(productId).orElseThrow(ProductNotFoundException::new);
		Account account = accountRepository.findByIdOptional(accountId).orElseThrow(AccountNotFoundException::new);

		Optional<Favorite> favorite = favoriteRepository
			.find("product.id = ?1 AND account.id = ?2", productId, accountId)
			.firstResultOptional();

		if (favorite.isPresent())
		{
			favoriteRepository.delete(favorite.get());
			return false;
		}
		else
		{
			Favorite newFavorite = new Favorite();
			newFavorite.setAccount(account);
			newFavorite.setProduct(product);
			favoriteRepository.persist(newFavorite);
			return true;
		}
	}
}
