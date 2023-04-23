import { PUBLIC_BACKEND_URL } from '$env/static/public';
import { Client, fetchExchange } from '@urql/svelte';

export const client = new Client({
  url: PUBLIC_BACKEND_URL,
  exchanges: [fetchExchange],
  fetchOptions: () => {
    const authHeader = localStorage.getItem('authHeader');
    return {
      headers: { authorization: authHeader || '' },
    };
  },
});