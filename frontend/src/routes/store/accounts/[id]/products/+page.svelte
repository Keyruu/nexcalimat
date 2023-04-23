<script lang="ts">
	import Product from '$lib/components/products/Product.svelte';
	import { GetProductsDocument, type GetProductsQuery } from '$lib/generated/graphql';
	import { getContextClient, queryStore } from '@urql/svelte';

	const products = queryStore<GetProductsQuery>({
		client: getContextClient(),
		query: GetProductsDocument
	});
</script>

<div class="products-grid">
	{#if $products.fetching}
		Loading products...
	{:else if $products.error}
		Something went wrong...
		{$products.error.graphQLErrors[0].extensions['code']}
	{:else if $products.data?.products?.data}
		<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
			{#each $products.data?.products?.data as product}
				{#if product}
					<Product product="{product}" />
				{/if}
			{/each}
		</div>
	{/if}
</div>

<style lang="scss">
	.products-grid {
		grid-area: 2 / 1 / 6 / 6;
	}
</style>
