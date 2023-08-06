package de.keyruu.nexcalimat.model.projection;

import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductPurchaseCount
{
	private final Product product;
	private final long count;

	public ProductPurchaseCount(Product product, long count)
	{
		this.product = product;
		this.count = count;
	}

	public Product getProduct()
	{
		return product;
	}

	public long getCount()
	{
		return count;
	}

	public long getRecommendedPurchaseAmount()
	{
		if (product.getType() == ProductType.COLD_DRINK)
		{
			return (long)Math.ceil((double)count / product.getBundleSize());
		}
		return 0;
	}
}
