package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.ProductService;

@GraphQLApi
public class ProductResource {
  @Inject
  ProductRepository _productRepository;

  @Inject
  ProductService _productService;

  @Query
  @Description("Get all Products")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public List<Product> products() {
    return _productRepository.listAll();
  }

  @Query
  @Description("Get Product by ID")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public Product product(Long id) {
    return _productRepository.findById(id);
  }

  @Mutation
  @Description("Update Product")
  @RolesAllowed(Roles.ADMIN)
  public Product product(Product product) {
    return _productService.updateProduct(product);
  }

  @Mutation
  @Description("Delete Account")
  @RolesAllowed(Roles.ADMIN)
  @Transactional
  public Boolean deleteAccount(Long id) {
    return Boolean.valueOf(_productRepository.deleteById(id));
  }
}
