import { PUBLIC_BACKEND_URL } from '$env/static/public';
import { authHeader } from '$lib/stores/authHeader';
import { Client, fetchExchange } from '@urql/svelte';
import { get } from 'svelte/store';

export const client = new Client({
  url: PUBLIC_BACKEND_URL,
  exchanges: [fetchExchange],
  fetchOptions: () => {
    const header = get(authHeader);
    return {
      headers: { authorization: header || '' },
    };
  },
});