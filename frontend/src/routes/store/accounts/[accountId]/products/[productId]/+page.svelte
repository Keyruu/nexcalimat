<script lang="ts">
	import { page } from '$app/stores';
	import ProductCheckout from '$lib/components/products/ProductCheckout.svelte';
	import {
		ProductByIdWithFavoriteDocument,
		type ProductByIdWithFavoriteQuery,
		type ProductByIdWithFavoriteQueryVariables
	} from '$lib/generated/graphql';
	import { getContextClient, queryStore } from '@urql/svelte';

	const client = getContextClient();
	const product = queryStore<ProductByIdWithFavoriteQuery, ProductByIdWithFavoriteQueryVariables>({
		client,
		query: ProductByIdWithFavoriteDocument,
		variables: {
			id: Number($page.params.productId)
		}
	});

	let amount = 1;
</script>

<div class="flex flex-col items-center justify-center">
	<h3 class="mb-2 font-bold">You are buying:</h3>
	{#if !$product.fetching && $product.data?.productWithFavorite}
		<ProductCheckout product="{$product.data?.productWithFavorite}" bind:amount="{amount}" />
	{/if}
</div>
