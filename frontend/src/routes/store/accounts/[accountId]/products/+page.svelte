<script lang="ts">
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import Product from '$lib/components/products/Product.svelte';
	import {
		ProductType,
		ProductsWithFavoritesDocument,
		type ProductsWithFavoritesQuery,
		type ProductsWithFavoritesQueryVariables
	} from '$lib/generated/graphql';
	import { error } from '$lib/utils/storeError';
	import { getContextClient, queryStore, type OperationResultStore } from '@urql/svelte';
	import { onDestroy, onMount } from 'svelte';
	import type { Unsubscriber } from 'svelte/store';

	let typeFilter: ProductType | undefined;

	let products: OperationResultStore<ProductsWithFavoritesQuery, ProductsWithFavoritesQueryVariables>;

	$: products = queryStore<ProductsWithFavoritesQuery, ProductsWithFavoritesQueryVariables>({
		client: getContextClient(),
		query: ProductsWithFavoritesDocument,
		variables: {
			type: typeFilter
		}
	});

	let unsubscribe: Unsubscriber;

	onMount(() => {
		unsubscribe = products.subscribe((result) => {
			if (result.error) {
				error(result.error);
			}
		});
	});

	function setFilter(type: ProductType) {
		if (typeFilter === type) {
			typeFilter = undefined;
		} else {
			typeFilter = type;
		}
	}

	onDestroy(() => {
		unsubscribe();
	});
</script>

<div class="w-full">
	<div class="mb-4 ml-6 flex flex-row items-center">
		<h3 class="unstyled text-3xl font-bold">Filter:&nbsp;</h3>
		<span
			class="chip mr-2 bg-blue-700 text-2xl {typeFilter === ProductType.ColdDrink
				? 'variant-filled-[blue]'
				: 'variant-soft'}"
			on:click="{() => setFilter(ProductType.ColdDrink)}"
			on:keypress
			role="button"
			tabindex="0"
		>
			cold
		</span>
		<span
			class="chip mr-2 text-2xl {typeFilter === ProductType.HotDrink ? 'variant-filled-error' : 'variant-soft'}"
			on:click="{() => setFilter(ProductType.HotDrink)}"
			on:keypress
			role="button"
			tabindex="0"
		>
			hot
		</span>
	</div>
	{#if $products.error}
		Something went wrong...
	{:else if $products.data?.productsWithFavorites?.data}
		<div class="grid grid-cols-2 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
			{#each $products.data?.productsWithFavorites?.data as product}
				{#if product}
					<a class="unstyled md:m-4" href="{base}/store/accounts/{$page.params.accountId}/products/{product.id}">
						<Product {product} />
					</a>
				{/if}
			{/each}
		</div>
	{/if}
</div>

<style lang="scss">
</style>
