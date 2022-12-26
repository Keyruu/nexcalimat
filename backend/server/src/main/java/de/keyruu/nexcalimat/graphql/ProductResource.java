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
public class ProductResource
{
  @Inject
  ProductRepository _productRepository;

  @Inject
  ProductService _productService;

  @Query
  @Description("Get all Products")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public List<Product> products()
  {
    return _productService.listAll();
  }

  @Query
  @Description("Get Product by ID")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public Product product(Long id)
  {
    return _productService.findById(id);
  }

  @Mutation
  @Description("Create Product")
  @RolesAllowed(Roles.ADMIN)
  @Transactional
  public Product createProduct(Product product)
  {
    _productRepository.persist(product);
    return product;
  }

  @Mutation
  @Description("Update Product")
  @RolesAllowed(Roles.ADMIN)
  public Product updateProduct(Product product)
  {
    return _productService.updateProduct(product);
  }

  @Mutation
  @Description("Update Product")
  @RolesAllowed(Roles.ADMIN)
  public Product updateProductPicture(Product product)
  {
    return _productService.updateProductPicture(product);
  }

  @Mutation
  @Description("Delete Product")
  @RolesAllowed(Roles.ADMIN)
  @Transactional
  public Boolean deleteProduct(Long id)
  {
    return _productService.deleteById(id);
  }
}
