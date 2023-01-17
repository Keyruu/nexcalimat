import { writable } from 'svelte/store';

export const accountToken = writable<string | undefined>(undefined);
