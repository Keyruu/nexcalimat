import { env } from '$env/dynamic/public';
import { authHeader } from '$lib/stores/authHeader';
import { Client, fetchExchange } from '@urql/svelte';
import { get } from 'svelte/store';

export const client = new Client({
  url: env.PUBLIC_BACKEND_URL,
  exchanges: [fetchExchange],
  fetchOptions: () => {
    const header = get(authHeader);
    return {
      headers: { authorization: header || '' },
    };
  },
});