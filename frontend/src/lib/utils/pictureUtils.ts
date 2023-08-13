
import { env } from '$env/dynamic/public';
import { ProductType, type Account, type Product, type ProductWithFavorite } from '$lib/generated/graphql';
import { authHeader } from '$lib/stores/authHeader';
import { get } from 'svelte/store';

const hotdrinkDefault = 'data:image/svg+xml,%3Csvg xmlns="http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg" width="340" height="412" viewBox="0 0 640 512"%3E%3Cpath fill="lightgray" d="M192 384h192c53 0 96-43 96-96h32c70.6 0 128-57.4 128-128S582.6 32 512 32H120c-13.3 0-24 10.7-24 24v232c0 53 43 96 96 96zM512 96c35.3 0 64 28.7 64 64s-28.7 64-64 64h-32V96h32zm47.7 384H48.3c-47.6 0-61-64-36-64h583.3c25 0 11.8 64-35.9 64z"%2F%3E%3C%2Fsvg%3E'
const colddrinkDefault = 'data:image/svg+xml,%3Csvg xmlns="http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg" width="284" height="512" viewBox="0 0 384 512"%3E%3Cpath fill="lightgray" d="M32 0C23.1 0 14.6 3.7 8.6 10.2S-.6 25.4.1 34.3l28.8 403.4c3 41.9 37.8 74.3 79.8 74.3h166.6c42 0 76.8-32.4 79.8-74.3l28.8-403.4c.6-8.9-2.4-17.6-8.5-24.1S360.9 0 352 0H32zm41 156.5L66.4 64h251.2l-6.6 92.5l-24.2 12.1c-19.4 9.7-42.2 9.7-61.6 0a74.556 74.556 0 0 0-66.4 0c-19.4 9.7-42.2 9.7-61.6 0L73 156.5z"%2F%3E%3C%2Fsvg%3E'

export function getProductPicture(product: ProductWithFavorite | Product): string {
  return product.picture ? `${env.PUBLIC_STORAGE_URL}/product/${product.picture}` : product.type === ProductType.HotDrink ? hotdrinkDefault : colddrinkDefault
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