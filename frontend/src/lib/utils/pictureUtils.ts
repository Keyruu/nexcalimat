
import { base } from '$app/paths';
import { env } from '$env/dynamic/public';
import type { Account, Product, ProductWithFavorite } from '$lib/generated/graphql';

export function getProductPicture(product: ProductWithFavorite | Product): string {
  return product.picture ? `${env.PUBLIC_STORAGE_URL}/product/${product.picture}` : `${base}/img/default_bottle.png`
}

export function getAccountPicture(account: Account): string {
  return account.picture ? `${env.PUBLIC_STORAGE_URL}/account/${account.picture}` : ''
}