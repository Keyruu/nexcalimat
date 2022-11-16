package de.keyruu.nexcalimat.repository;

import javax.enterprise.context.ApplicationScoped;

import de.keyruu.nexcalimat.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
