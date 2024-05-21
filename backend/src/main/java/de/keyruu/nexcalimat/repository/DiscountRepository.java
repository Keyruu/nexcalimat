package de.keyruu.nexcalimat.repository;

import de.keyruu.nexcalimat.model.Discount;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiscountRepository implements PanacheRepository<Discount>
{
	public int getDiscount()
	{
		return findAll().firstResultOptional().map(Discount::getPercentage).orElse(0);
	}
}
