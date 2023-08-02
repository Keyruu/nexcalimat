package de.keyruu.nexcalimat.model.projection;

import java.time.LocalDateTime;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductWithFavorite
{
	private final Long id;
	private final LocalDateTime createdAt;
	private final LocalDateTime deletedAt;
	private final String name;
	private final String picture;
	private final Integer price;
	private final Integer bundleSize;
	private final ProductType type;
	private final Boolean isFavorite;

	public ProductWithFavorite(Product product, Boolean isFavorite)
	{
		this.id = product.getId();
		this.createdAt = product.getCreatedAt();
		this.deletedAt = product.getDeletedAt();
		this.name = product.getName();
		this.picture = product.getPicture();
		this.price = product.getPrice();
		this.bundleSize = product.getBundleSize();
		this.type = product.getType();
		this.isFavorite = isFavorite;
	}

	public Long getId()
	{
		return id;
	}

	public LocalDateTime getCreatedAt()
	{
		return createdAt;
	}

	public LocalDateTime getDeletedAt()
	{
		return deletedAt;
	}

	public String getName()
	{
		return name;
	}

	public String getPicture()
	{
		return picture;
	}

	public Integer getPrice()
	{
		return price;
	}

	public Integer getBundleSize()
	{
		return bundleSize;
	}

	public ProductType getType()
	{
		return type;
	}

	public Boolean getIsFavorite()
	{
		return isFavorite;
	}
}
