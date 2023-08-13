
import { base } from '$app/paths';
import { env } from '$env/dynamic/public';
import type { Account, Product, ProductWithFavorite } from '$lib/generated/graphql';
import { authHeader } from '$lib/stores/authHeader';
import { get } from 'svelte/store';

export function getProductPicture(product: ProductWithFavorite | Product): string {
  return product.picture ? `${env.PUBLIC_STORAGE_URL}/product/${product.picture}` : `${base}/img/default_bottle.png`
}

export function getAccountPicture(account: Account): string {
  return account.picture ? `${env.PUBLIC_STORAGE_URL}/account/${account.picture}` : ''
}

function getFormData(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('filename', file.name);
  return formData;
}

export async function uploadAccountImage(file: File, account: Account) {
  const formData = getFormData(file);
  await fetch(`${env.PUBLIC_BACKEND_URL}/api/v1/picture/account/${account.id}`, {
    method: 'POST',
    headers: {
      Authorization: get(authHeader)!
    },
    body: formData
  });
}

export async function uploadMyAccountImage(file: File) {
  const formData = getFormData(file);
  await fetch(`${env.PUBLIC_BACKEND_URL}/api/v1/picture/myAccount`, {
    method: 'POST',
    headers: {
      Authorization: get(authHeader)!
    },
    body: formData
  });
}

export async function uploadProductImage(file: File, product: Product): Promise<Response> {
  const formData = getFormData(file);
  return fetch(`${env.PUBLIC_BACKEND_URL}/api/v1/picture/product/${product.id}`, {
    method: 'POST',
    headers: {
      Authorization: get(authHeader)!
    },
    body: formData
  });
}

export async function deleteProductImage(product: Product): Promise<Response> {
  return fetch(`${env.PUBLIC_BACKEND_URL}/api/v1/picture/product/${product.id}`, {
    method: 'DELETE',
    headers: {
      Authorization: get(authHeader)!
    }
  });
}