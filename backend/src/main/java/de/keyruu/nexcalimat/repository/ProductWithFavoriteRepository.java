package de.keyruu.nexcalimat.repository;

import de.keyruu.nexcalimat.model.ProductWithFavorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductWithFavoriteRepository implements PanacheRepository<ProductWithFavorite>
{
	//
}
