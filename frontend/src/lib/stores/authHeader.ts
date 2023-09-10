import { localStorageStore } from '@skeletonlabs/skeleton';
import type { Writable } from 'svelte/store';
import {writable} from "svelte/store";

export const authHeader: Writable<string> = localStorageStore('authHeader', "");
export const pinHeader: Writable<string | null> = writable(null);
