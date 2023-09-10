import { env } from '$env/dynamic/public';
import {authHeader, pinHeader} from '$lib/stores/authHeader';
import { Client, fetchExchange } from '@urql/svelte';
import { get } from 'svelte/store';

export const client = new Client({
  url: `${env.PUBLIC_BACKEND_URL}/graphql`,
  exchanges: [fetchExchange],
  fetchOptions: () => {
    const header = get(authHeader);
    return {
      headers: { authorization: header },
    };
  },
});

export const storeClient = new Client({
  url: `${env.PUBLIC_BACKEND_URL}/graphql`,
  exchanges: [fetchExchange],
  fetchOptions: () => {
    const header = get(pinHeader);
    return {
      headers: { authorization: header ? `PIN ${header}` : '' },
    };
  },
});
