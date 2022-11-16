package de.keyruu.nexcalimat.repository;

import javax.enterprise.context.ApplicationScoped;

import de.keyruu.nexcalimat.model.Purchase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PurchaseRepository implements PanacheRepository<Purchase> {
}
