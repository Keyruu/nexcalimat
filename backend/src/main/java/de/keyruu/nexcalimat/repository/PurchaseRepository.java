package de.keyruu.nexcalimat.repository;

import de.keyruu.nexcalimat.model.Purchase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PurchaseRepository implements PanacheRepository<Purchase>
{
	//
}
