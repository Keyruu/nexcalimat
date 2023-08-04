package de.keyruu.nexcalimat.model.projection;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.SimpleProduct;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductWithFavorite extends SimpleProduct
{
	private final Boolean isFavorite;

	public ProductWithFavorite(Product product, Boolean isFavorite)
	{
		setId(product.getId());
		setCreatedAt(product.getCreatedAt());
		setDeletedAt(product.getDeletedAt());
		setName(product.getName());
		setPicture(product.getPicture());
		setPrice(product.getPrice());
		setBundleSize(product.getBundleSize());
		setType(product.getType());
		this.isFavorite = isFavorite;
	}

	public Boolean getIsFavorite()
	{
		return isFavorite;
	}
}
