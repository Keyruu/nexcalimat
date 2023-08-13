<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import {
		MakePurchaseDocument,
		ToggleFavoriteDocument,
		type MakePurchaseMutation,
		type MakePurchaseMutationVariables,
		type ProductWithFavorite,
		type ToggleFavoriteMutation,
		type ToggleFavoriteMutationVariables
	} from '$lib/generated/graphql';
	import { authHeader } from '$lib/stores/authHeader';
	import { centToEuro } from '$lib/utils/formatEuro';
	import { getProductPicture } from '$lib/utils/pictureUtils';
	import { handleError } from '$lib/utils/storeError';
	import { toastSuccess } from '$lib/utils/toastUtils';
	import Icon from '@iconify/svelte';
	import { toastStore } from '@skeletonlabs/skeleton';
	import { getContextClient, mutationStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';

	export let product: ProductWithFavorite;
	export let amount: number;

	const client = getContextClient();
	const toggleFavorite = () =>
		mutationStore<ToggleFavoriteMutation, ToggleFavoriteMutationVariables>({
			client,
			query: ToggleFavoriteDocument,
			variables: {
				id: product.id
			}
		});

	let favoriteUnsubscribe: Unsubscriber;

	const makePurchase = () =>
		mutationStore<MakePurchaseMutation, MakePurchaseMutationVariables>({
			client,
			query: MakePurchaseDocument,
			variables: {
				id: product.id,
				amount
			}
		});

	let makePurchaseUnsubscribe: Unsubscriber;

	function getCancelHref(path: string): string {
		const split = path.split('/');
		split.pop();
		return split.join('/');
	}

	function checkout() {
		makePurchaseUnsubscribe = makePurchase().subscribe((result) => {
			if (result.data?.makePurchase) {
				console.log(result.data?.makePurchase);
				toastStore.trigger(toastSuccess($_('toast.purchase.success')));
				authHeader.set('');
				goto(`${base}/store/accounts`);
			} else if (result.error) {
				handleError(result.error);
			}
		});
	}

	onDestroy(() => {
		if (favoriteUnsubscribe) {
			favoriteUnsubscribe();
		}
		if (makePurchaseUnsubscribe) {
			makePurchaseUnsubscribe();
		}
	});
</script>

<div class="card variant-glass-surface flex md:w-3/4 flex-col items-center justify-center pb-8">
	<div class="absolute right-0 top-0 m-4">
		<button
			type="button"
			class="btn btn-icon variant-glass-surface"
			on:click="{() => {
				favoriteUnsubscribe = toggleFavorite().subscribe((result) => {
					if (!result.fetching && result.data?.toggleFavorite != null) {
						product.isFavorite = result.data.toggleFavorite;
					}
				});
			}}"
		>
			<Icon
				icon="{product.isFavorite ? 'mdi:cards-heart' : 'mdi:cards-heart-outline'}"
				class="h-5 w-5 {product.isFavorite ? 'text-pink-600' : ''}"
			/>
		</button>
	</div>
	<figure class="m-10 md:m-4 mb-0 flex items-center justify-center py-4 sm:h-80">
		<img class="max-h-full max-w-full" src="{getProductPicture(product)}" alt="{product.name}" />
	</figure>
	<h2 class="mx-1 my-2 text-center font-bold">{product.name}</h2>
	<h3 class="my-2 font-bold" use:centToEuro>{product.price}</h3>
	<div class="flex flex-row items-center justify-center">
		<div class="btn-group variant-ghost my-2">
			<button
				on:click="{() => {
					if (amount !== 1) amount--;
				}}">-</button
			>
			<button on:click="{() => (amount = 1)}">{amount}</button>
			<button on:click="{() => amount++}">+</button>
		</div>
	</div>
</div>
<div class="mt-2 flex w-full md:w-3/4">
	<a
		href="{getCancelHref($page.url.pathname)}"
		type="button"
		class="md:h-20 md:w-20 btn btn-icon btn-icon-lg variant-ghost-error"
	>
		<Icon icon="material-symbols:cancel-outline-rounded" class="h-6 w-6 text-error-500" />
	</a>
	<div class="flex-1"></div>
	<button
		on:click="{checkout}"
		type="button"
		class="md:h-20 md:w-20 btn btn-icon btn-icon-lg variant-ghost-success ml-auto"
	>
		<Icon icon="material-symbols:shopping-cart-checkout-rounded" class="h-6 w-6 text-success-500" />
	</button>
</div>
