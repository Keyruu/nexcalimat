<script lang="ts">
	import Products from '$lib/components/admin/Products.svelte';
	import SearchField from '$lib/components/admin/SearchField.svelte';
	import { ProductsDocument, type ProductsQuery, type ProductsQueryVariables } from '$lib/generated/graphql';
	import Icon from '@iconify/svelte';
	import { queryStore, type OperationResultStore, type QueryArgs } from '@urql/svelte';
	import { client } from '../../../urqlClient';

	let searchByName = '';
	let products: OperationResultStore<ProductsQuery>;
	let productsQueryOptions: QueryArgs<ProductsQuery, ProductsQueryVariables>;

	$: {
		productsQueryOptions = {
			client,
			query: ProductsDocument,
			variables: {
				searchByName: searchByName === '' ? undefined : searchByName
			}
		};
		products = queryStore<ProductsQuery, ProductsQueryVariables>(productsQueryOptions);
	}

	function refetch() {
		products = queryStore<ProductsQuery, ProductsQueryVariables>({
			...productsQueryOptions,
			requestPolicy: 'network-only'
		});
	}
</script>

<div class="flex flex-col">
	<div class="flex flex-row items-center">
		<SearchField on:search="{(search) => (searchByName = search.detail)}" />
		<span class="divider-vertical h-8 ml-6 mr-6"></span>
		<button class="btn variant-filled"><Icon icon="fa-solid:plus" />&nbsp;Create Product</button>
	</div>
	{#if $products.data?.products?.data && $products.data.products.data.length > 0}
		<Products products="{$products.data.products.data}" on:refetch="{refetch}" />
	{/if}
</div>
