import { writable } from 'svelte/store';

export const authHeader = writable<string | undefined>(undefined);
