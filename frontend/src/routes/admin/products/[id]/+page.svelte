<script lang="ts">
	import { page } from '$app/stores';
	import ProductForm from '$lib/components/admin/ProductForm.svelte';
	import {
		ProductByIdDocument,
		ProductType,
		type Product,
		type ProductByIdQuery,
		type ProductByIdQueryVariables
	} from '$lib/generated/graphql';
	import { handleError } from '$lib/utils/storeError';
	import { queryStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import { client } from '../../../../urqlClient';

	let productBind: Product;
	const product = queryStore<ProductByIdQuery, ProductByIdQueryVariables>({
		client,
		query: ProductByIdDocument,
		variables: {
			id: Number($page.params.id)
		}
	});

	const unsubscriber = product.subscribe(({ data, error }) => {
		if (data?.product) {
			productBind = JSON.parse(JSON.stringify(data.product));
			if (productBind.price) productBind.price = productBind.price / 100;
			if (productBind.type === ProductType.HotDrink) productBind.bundleSize = 0;
		} else if (error) {
			handleError(error);
		}
	});

	onDestroy(() => {
		unsubscriber();
	});
</script>

<ProductForm {productBind} />
