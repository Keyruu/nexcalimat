package de.keyruu.nexcalimat.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.graphql.Ignore;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.validation.constraints.Size;

@Entity
public class Product implements HasPicture
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(nullable = false)
	private String name;

	@Size(max = 3000)
	@Column(length = 3000)
	private String picture;

	@Column(nullable = false)
	private Integer price;

	@Column(name = "bundle_size", nullable = false)
	private Integer bundleSize;

	@Column(nullable = false)
	private ProductType type;

	@Ignore
	@OneToMany(mappedBy = "product")
	private Set<Purchase> purchases = new HashSet<>();

	@Ignore
	@OneToMany(mappedBy = "product")
	private Set<Favorite> favorites = new HashSet<>();

	@Override
	public Long getId()
	{
		return this.id;
	}

	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalDateTime getCreatedAt()
	{
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt)
	{
		this.createdAt = createdAt;
	}

	public LocalDateTime getDeletedAt()
	{
		return this.deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt)
	{
		this.deletedAt = deletedAt;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getPicture()
	{
		return this.picture;
	}

	@Override
	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public Integer getPrice()
	{
		return this.price;
	}

	public void setPrice(Integer price)
	{
		this.price = price;
	}

	public Integer getBundleSize()
	{
		return this.bundleSize;
	}

	public void setBundleSize(Integer bundleSize)
	{
		this.bundleSize = bundleSize;
	}

	public ProductType getType()
	{
		return this.type;
	}

	public void setType(ProductType type)
	{
		this.type = type;
	}

	public Set<Purchase> getPurchases()
	{
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases)
	{
		this.purchases = purchases;
	}

	public Set<Favorite> getFavorites()
	{
		return favorites;
	}

	public void setFavorites(Set<Favorite> favorites)
	{
		this.favorites = favorites;
	}

	@PreRemove
	void beforeDelete()
	{
		this.purchases.forEach(p -> p.setProduct(null));
	}
}
