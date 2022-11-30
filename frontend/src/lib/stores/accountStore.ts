import type { Account } from '$lib/graphql/generated/graphql';
import { writable } from 'svelte/store';

export const loggedInAccount = writable<Account | null>(null);
