import type { Account } from '$lib/types/Account';
import { writable } from 'svelte/store';

export const loggedInAccount = writable<Account | null>(null);
