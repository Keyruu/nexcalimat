package de.keyruu.nexcalimat.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.graphql.Ignore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;

@Entity
public class Product extends SimpleProduct
{
	@Ignore
	@OneToMany(mappedBy = "product")
	private Set<Purchase> purchases = new HashSet<>();

	@Ignore
	@OneToMany(mappedBy = "product")
	private Set<Favorite> favorites = new HashSet<>();

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
		getPurchases().forEach(p -> p.setProduct(null));
	}
}
