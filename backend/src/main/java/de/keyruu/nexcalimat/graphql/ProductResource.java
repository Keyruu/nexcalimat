package de.keyruu.nexcalimat.graphql;

import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PagePojo;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.graphql.pojo.SortPojo;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.ProductWithFavorite;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.FavoriteService;
import de.keyruu.nexcalimat.service.ProductService;
import io.quarkus.panache.common.Sort;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GraphQLApi
public class ProductResource
{
	@Inject
	ProductRepository _productRepository;

	@Inject
	ProductService _productService;

	@Inject
	FavoriteService _favoriteService;

	@Inject
	CurrentVertxRequest _request;

	@Inject
	JwtUtils _jwtUtils;

	@Query
	@Description("Get all Products")
	@RolesAllowed({ Roles.CUSTOMER, Roles.USER })
	public PaginationResponse<Product> products(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return _productService.listAll(Mapper.map(page, sort));
	}

	@Query
	@Description("Get all Products with Favorites")
	@RolesAllowed({ Roles.CUSTOMER })
	public PaginationResponse<ProductWithFavorite> productsWithFavorites(
		Optional<PagePojo> page,
		Optional<SortPojo> sort,
		Optional<ProductType> type)
	{
		return _productService
			.listAllWithFavorites(Mapper.map(page, sort, Sort.descending("isFavorite")),
				_jwtUtils.getPinJwtAccountId(_request), type);
	}

	@Query
	@Description("Get Product by ID")
	@RolesAllowed({ Roles.CUSTOMER, Roles.USER })
	public Product product(Long id)
	{
		return _productService.findById(id);
	}

	@Query
	@Description("Get ProductWithFavorite by ID")
	@RolesAllowed(Roles.CUSTOMER)
	public ProductWithFavorite productWithFavorite(Long id)
	{
		return _productService.findByIdWithFavorite(id, _jwtUtils.getPinJwtAccountId(_request));
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
	@Description("Delete Product")
	@RolesAllowed(Roles.ADMIN)
	public Boolean deleteProduct(Long id)
	{
		return _productService.deleteById(id);
	}

	@Mutation
	@Description("Toggle favorite")
	@RolesAllowed(Roles.CUSTOMER)
	public Boolean toggleFavorite(Long productId)
	{
		return _favoriteService.toggleFavorite(productId, _jwtUtils.getPinJwtAccountId(_request));
	}
}
