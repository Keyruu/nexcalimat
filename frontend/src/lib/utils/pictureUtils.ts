
import { base } from '$app/paths';
import { PUBLIC_STORAGE_URL } from '$env/static/public';
import type { Product, ProductWithFavorite } from '$lib/generated/graphql';

export function getProductPicture(product: ProductWithFavorite | Product): string {
  return product.picture ? `${PUBLIC_STORAGE_URL}/product/${product.picture}` : `${base}/img/default_bottle.png`
}