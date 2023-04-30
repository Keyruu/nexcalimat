<script lang="ts">
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import Product from '$lib/components/products/Product.svelte';
	import { GetProductsWithFavoritesDocument, type GetProductsWithFavoritesQuery } from '$lib/generated/graphql';
	import { getContextClient, queryStore } from '@urql/svelte';

	const products = queryStore<GetProductsWithFavoritesQuery>({
		client: getContextClient(),
		query: GetProductsWithFavoritesDocument
	});
</script>

<div class="w-full">
	{#if $products.fetching}
		Loading products...
	{:else if $products.error}
		Something went wrong...
	{:else if $products.data?.productsWithFavorites?.data}
		<div></div>
		<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
			{#each $products.data?.productsWithFavorites?.data as product}
				{#if product}
					<a class="unstyled" href="{base}/store/accounts/{$page.params.accountId}/products/{product.id}">
						<Product product="{product}" />
					</a>
				{/if}
			{/each}
		</div>
	{/if}
</div>

<style lang="scss">
</style>
