package de.keyruu.nexcalimat.repository;

import java.util.List;
import java.util.Optional;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.projection.ProductWithFavorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product>
{
	public List<ProductWithFavorite> findAllWithFavorite(Long accountId, Mapper mapper, Optional<ProductType> type)
	{
		Tuple2<String, Parameters> tuple = buildQueryAndParams(accountId, type, false, Optional.empty());
		return find(tuple.getItem1(), mapper.getSort(), tuple.getItem2())
			.project(ProductWithFavorite.class)
			.page(mapper.getPage())
			.list();
	}

	public ProductWithFavorite findProductWithFavoriteById(Long id, Long accountId)
	{
		Tuple2<String, Parameters> tuple = buildQueryAndParams(accountId, Optional.empty(), false, Optional.of(id));
		return find(tuple.getItem1(), tuple.getItem2())
			.project(ProductWithFavorite.class)
			.firstResult();
	}

	public long findAllWithFavoriteCount(Long accountId, Optional<ProductType> type)
	{
		Tuple2<String, Parameters> tuple = buildQueryAndParams(accountId, type, true, Optional.empty());
		return find(tuple.getItem1(), tuple.getItem2())
			.count();
	}

	private Tuple2<String, Parameters> buildQueryAndParams(Long accountId, Optional<ProductType> type, boolean isCount, Optional<Long> productId)
	{
		String query = "SELECT p";
		query += !isCount ? ", f.account IS NOT NULL AS isFavorite " : " ";
		query += "FROM Product p LEFT JOIN p.favorites f WITH f.account.id = :accountId WHERE p.deletedAt IS NULL";
		Parameters params = Parameters.with("accountId", accountId);

		if (type.isPresent())
		{
			query += " AND type = :type";
			params = params.and("type", type.get());
		}

		if (productId.isPresent())
		{
			query += " AND p.id = :productId";
			params = params.and("productId", productId.get());
		}

		return Tuple2.of(query, params);
	}
}
