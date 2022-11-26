package de.keyruu.nexcalimat.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.repository.ProductRepository;

@ApplicationScoped
public class ProductService {
  @Inject
  ProductRepository _productRepository;

  public Product updateProduct(Product product) {
    Product dbProduct = _productRepository.findByIdOptional(product.getId()).orElseThrow(ProductNotFoundException::new);

    dbProduct.setBundleSize(product.getBundleSize());
    dbProduct.setName(product.getName());
    dbProduct.setPicture(product.getPicture());
    dbProduct.setPrice(product.getPrice());
    dbProduct.setType(product.getType());

    _productRepository.persist(dbProduct);

    return dbProduct;
  }
}
