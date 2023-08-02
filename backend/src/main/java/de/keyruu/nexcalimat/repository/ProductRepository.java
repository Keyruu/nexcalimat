package de.keyruu.nexcalimat.repository;

import java.util.List;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductWithFavorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product>
{
	public List<ProductWithFavorite> findAllWithFavorite(Long accountId)
	{
		return getEntityManager().createQuery("SELECT p, f.account FROM Product p LEFT JOIN p.favorites f WITH f.account.id = :accountId", ProductWithFavorite.class)..setParameter("accountId", accountId).getResultList();
	}
}
