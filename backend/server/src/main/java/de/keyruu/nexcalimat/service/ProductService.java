package de.keyruu.nexcalimat.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.repository.ProductRepository;

@ApplicationScoped
public class ProductService
{
  @Inject
  ProductRepository _productRepository;

  @Transactional
  public Product updateProductPicture(Product product)
  {
    Product dbProduct = _productRepository.findByIdOptional(product.getId()).orElseThrow(ProductNotFoundException::new);

    dbProduct.setPicture(product.getPicture());
    _productRepository.persist(dbProduct);

    return dbProduct;
  }

  @Transactional
  public Product updateProduct(Product product)
  {
    Product dbProduct = _productRepository.findByIdOptional(product.getId()).orElseThrow(ProductNotFoundException::new);

    if (product.getBundleSize() != null)
    {
      dbProduct.setBundleSize(product.getBundleSize());
    }
    if (product.getName() != null)
    {
      dbProduct.setName(product.getName());
    }
    if (product.getPrice() != null)
    {
      dbProduct.setPrice(product.getPrice());
    }
    if (product.getType() != null)
    {
      dbProduct.setType(product.getType());
    }

    _productRepository.persist(dbProduct);

    return dbProduct;
  }
}
