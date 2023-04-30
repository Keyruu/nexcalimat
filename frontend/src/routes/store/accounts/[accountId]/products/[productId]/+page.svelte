<script lang="ts">
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import { PUBLIC_STORAGE_URL } from '$env/static/public';
	import { ProductByIdDocument, type ProductByIdQuery, type ProductByIdQueryVariables } from '$lib/generated/graphql';
	import { centToEuro } from '$lib/utils/formatEuro';
	import Icon from '@iconify/svelte';
	import { getContextClient, queryStore } from '@urql/svelte';

	const client = getContextClient();
	const product = queryStore<ProductByIdQuery, ProductByIdQueryVariables>({
		client,
		query: ProductByIdDocument,
		variables: {
			id: Number($page.params.productId)
		}
	});

	let amount = 1;
</script>

<div class="flex flex-col items-center justify-center">
	<h3 class="mb-2 font-bold">You are buying:</h3>
	{#if !$product.fetching && $product.data?.product}
		<div class="card card-hover variant-glass-surface flex h-[34rem] w-80 flex-col items-center justify-center">
			<div class="absolute right-0 top-0 m-4">
				<button type="button" class="btn btn-icon variant-glass-surface">
					<Icon icon="mdi:cards-heart-outline" class="h-5 w-5" />
				</button>
			</div>
			<figure class="m-4 mb-0 flex items-center justify-center py-4 sm:h-80">
				<img
					class="max-h-full max-w-full"
					src="{$product.data.product.picture
						? `${PUBLIC_STORAGE_URL}/product/${$product.data.product.picture}`
						: `${base}/img/default_bottle.png`}"
					alt="{$product.data.product.name}"
				/>
			</figure>
			<h2 class="my-2 text-center font-bold">{$product.data.product.name}</h2>
			{#if $product.data.product.price}
				<h3 class="font-bold" use:centToEuro>{$product.data.product.price}</h3>
			{/if}
			<div class="flex flex-row items-center justify-center">
				<div class="btn-group variant-ghost my-2">
					<button
						on:click="{() => {
							if (amount !== 1) amount--;
						}}">-</button
					>
					<button>{amount}</button>
					<button on:click="{() => amount++}">+</button>
				</div>
			</div>
		</div>
		<div class="mt-2 flex w-80">
			<button type="button" class="btn btn-icon btn-icon-lg variant-ghost-error">
				<Icon icon="material-symbols:cancel-outline-rounded" class="h-6 w-6 text-error-500" />
			</button>
			<div class="flex-1"></div>
			<button type="button" class="btn btn-icon btn-icon-lg variant-ghost-success ml-auto">
				<Icon icon="material-symbols:shopping-cart-checkout-rounded" class="h-6 w-6 text-success-500" />
			</button>
		</div>
	{/if}
</div>
