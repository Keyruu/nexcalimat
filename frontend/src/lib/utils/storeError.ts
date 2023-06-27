import { goto } from "$app/navigation";
import { base } from "$app/paths";
import { toastStore } from "@skeletonlabs/skeleton";
import type { CombinedError } from "@urql/svelte";

export function error(error: CombinedError) {
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
  toastStore.trigger({
    message: 'You were logged out. Please log in again.',
    classes: 'text-red-300',
    background: 'variant-ghost-error'
  });
  goto(`${base}/store/accounts`);
}