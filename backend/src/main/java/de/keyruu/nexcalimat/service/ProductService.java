package de.keyruu.nexcalimat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.PictureType;
import de.keyruu.nexcalimat.graphql.exception.IllegalOperationException;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.projection.ProductWithFavorite;
import de.keyruu.nexcalimat.repository.ProductRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService
{
	@Inject
	ProductRepository productRepository;

	@Inject
	PictureService pictureService;

	public PaginationResponse<Product> listAll(Mapper mapper, Optional<String> searchByName)
	{
		String query = "deletedAt IS NULL";
		PanacheQuery<Product> panacheQuery;
		long count;
		if (searchByName.isPresent())
		{
			query += " AND LOWER(name) LIKE :name";
			Parameters parameters = Parameters.with("name", "%" + searchByName.get().toLowerCase() + "%");

			panacheQuery = productRepository.find(query, mapper.getSort(), parameters);
			count = productRepository.count(query, parameters);
		}
		else
		{
			panacheQuery = productRepository.find(query, mapper.getSort());
			count = productRepository.count(query);
		}
		List<Product> products = panacheQuery
			.page(mapper.getPage()).list();

		return new PaginationResponse<>(products, count, mapper);
	}

	public PaginationResponse<ProductWithFavorite> listAllWithFavorites(Mapper mapper, Long accountId, Optional<ProductType> type)
	{
		List<ProductWithFavorite> products = productRepository.findAllWithFavorite(accountId, mapper, type);
		long count = productRepository.findAllWithFavoriteCount(accountId, type);
		return new PaginationResponse<>(products, count, mapper);
	}

	public ProductWithFavorite findByIdWithFavorite(Long id, Long accountId)
	{
		return productRepository.findProductWithFavoriteById(id, accountId);
	}

	public Product findById(Long id)
	{
		return productRepository.findById(id);
	}

	@Transactional
	public Product updateProductPicture(Long id, FileFormData formData) throws IOException
	{
		Product dbProduct = productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		return pictureService.updatePicture(dbProduct, formData, productRepository, PictureType.PRODUCT);
	}

	@Transactional
	public void deleteProductPicture(Long id)
	{
		Product dbProduct = productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		pictureService.deletePicture(dbProduct, productRepository, PictureType.PRODUCT);
	}

	@Transactional
	public Product updateProduct(Product product)
	{
		Product dbProduct = productRepository.findByIdOptional(product.getId())
			.orElseThrow(ProductNotFoundException::new);

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

		productRepository.persist(dbProduct);

		return dbProduct;
	}

	@Transactional
	public Boolean reactivateProduct(Long id)
	{
		Product dbProduct = productRepository.findByIdOptional(id)
			.orElseThrow(ProductNotFoundException::new);

		if (dbProduct.getDeletedAt() == null)
		{
			throw new IllegalOperationException();
		}

		dbProduct.setDeletedAt(null);
		productRepository.persist(dbProduct);
		return true;
	}

	@Transactional
	public Boolean deleteById(Long id)
	{
		Product product = productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		if (product.getDeletedAt() != null)
		{
			throw new IllegalOperationException();
		}

		product.setDeletedAt(LocalDateTime.now());
		productRepository.persist(product);

		return Boolean.TRUE;
	}

	@Transactional
	public Boolean eraseById(Long id)
	{
		return productRepository.deleteById(id);
	}
}