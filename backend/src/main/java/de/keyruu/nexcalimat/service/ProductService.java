package de.keyruu.nexcalimat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.PictureType;
import de.keyruu.nexcalimat.graphql.exception.ProductNotFoundException;
import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.projection.ProductWithFavorite;
import de.keyruu.nexcalimat.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService
{
	@Inject
	ProductRepository _productRepository;

	@Inject
	PictureService _pictureService;

 	public PaginationResponse<Product> listAll(Mapper mapper, Optional<String> searchByName)
 	{
 		String query = "deletedAt IS NULL";
   		if (searchByName.isPresent())
   		{
   			query += " AND LOWER(name) LIKE :name";
   		}
   		List<Product> products = _productRepository.find(query, Parameters.with("name", "%" + searchByName.get().toLowerCase() + "%"), mapper.getSort()).page(mapper.getPage()).list();
 		long count = _productRepository.count(query);
 		return new PaginationResponse<>(products, count, mapper);
 	}

	public PaginationResponse<ProductWithFavorite> listAllWithFavorites(Mapper mapper, Long accountId, Optional<ProductType> type)
	{
		List<ProductWithFavorite> products = _productRepository.findAllWithFavorite(accountId, mapper, type);
		long count = _productRepository.findAllWithFavoriteCount(accountId, type);
		return new PaginationResponse<>(products, count, mapper);
	}

	public ProductWithFavorite findByIdWithFavorite(Long id, Long accountId)
	{
		return _productRepository.findProductWithFavoriteById(id, accountId);
	}

	public Product findById(Long id)
	{
		return _productRepository.findById(id);
	}

	@Transactional
	public Product updateProductPicture(Long id, FileFormData formData) throws IOException
	{
		Product dbProduct = _productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		return _pictureService.updatePicture(dbProduct, formData, _productRepository, PictureType.PRODUCT);
	}

	@Transactional
	public void deleteProductPicture(Long id)
	{
		Product dbProduct = _productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		_pictureService.deletePicture(dbProduct, _productRepository, PictureType.PRODUCT);
	}

	@Transactional
	public Product updateProduct(Product product)
	{
		Product dbProduct = _productRepository.findByIdOptional(product.getId())
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

		_productRepository.persist(dbProduct);

		return dbProduct;
	}

	@Transactional
	public Boolean reactivateProduct(Long id)
	{
		Product dbProduct = _productRepository.findByIdOptional(id)
			.orElseThrow(ProductNotFoundException::new);

		dbProduct.setDeletedAt(null);
		_productRepository.persist(dbProduct);
		return true;
	}

	@Transactional
	public Boolean deleteById(Long id)
	{
		Product product = _productRepository.findByIdOptional(id).orElseThrow(ProductNotFoundException::new);

		product.setDeletedAt(LocalDateTime.now());
		_productRepository.persist(product);

		return Boolean.TRUE;
	}

	@Transactional
	public Boolean eraseById(Long id)
	{
		return _productRepository.deleteById(id);
	}
}