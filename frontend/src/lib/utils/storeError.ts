import { goto } from "$app/navigation";
import { base } from "$app/paths";
import { toastStore } from "@skeletonlabs/skeleton";
import type { CombinedError } from "@urql/svelte";
import { toastError } from "./toastUtils";

import { page } from "$app/stores";
import { _ } from 'svelte-i18n';
import { get } from "svelte/store";
export function handleError(error: CombinedError) {
  console.error(error);
  if (error.graphQLErrors.length > 0) {
    if (error.graphQLErrors[0].extensions?.code === 'unauthorized') {
      unauthorized();
    }
  } else if (error.networkError) {
    if (error.networkError.message === 'Unauthorized') {
      unauthorized();
    }
  }
}

function unauthorized() {
  if (get(page).route.id?.startsWith('/store')) {
    toastStore.trigger(toastError(get(_)('toast.unauthorized')));
    goto(`${base}/store/accounts`);
  }
}