<script lang="ts">
	import { ProductType, type Product, type ProductWithFavorite } from '$lib/generated/graphql';
	import { numberCentToEuro } from '$lib/utils/formatEuro';
	import { getProductPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';

	export let product: ProductWithFavorite | Product;
	export let adminMode = false;
</script>

<div
	class="sm:w-50 card card-hover variant-glass-surface flex cursor-pointer flex-col shadow-md hover:variant-ghost-surface hover:shadow-xl lg:h-[22rem] lg:w-64 relative"
>
	{#if adminMode}
		<div class="absolute right-0 top-0">
			<a class="btn-icon hover:variant-soft" href="/admin/products/{product.id}"><Icon icon="fa-solid:pencil-alt" /></a>
			<button class="btn-icon hover:variant-soft"><Icon icon="fa-solid:archive" /></button>
		</div>
	{/if}
	<figure class="m-4 mb-0 flex items-center justify-center py-4 h-36 md:h-44">
		<img class="max-h-full max-w-full" src="{getProductPicture(product)}" alt="{product.name}" />
	</figure>
	<div class="flex flex-1 flex-col items-center text-center">
		<div class="flex flex-1 items-center justify-center">
			<h2 class="unstyled line-clamp-2 font-bold {adminMode ? 'text-2xl' : 'sm:text-3xl lg:text-4xl'}">
				{product.name}
			</h2>
		</div>
		<div class="my-4">
			{#if product.__typename === 'ProductWithFavorite' && product.isFavorite}
				<div class="badge bg-pink-500">♥</div>
			{/if}
			{#if product.price && product.type}
				<div class="badge variant-filled-surface font-bold">
					{numberCentToEuro(product.price)}
				</div>
				<div
					class="
					badge
					font-bold
					{product.type === 'HOT_DRINK' ? 'bg-red-700' : 'bg-blue-700'}"
				>
					{product.type === 'HOT_DRINK' ? 'hot' : 'cold'}
				</div>
			{/if}
			{#if adminMode && product.type === ProductType.ColdDrink}
				<div class="badge variant-filled-surface font-bold">
					{product.bundleSize}&nbsp;
					<Icon icon="fa-solid:box" />
				</div>
			{/if}
		</div>
	</div>
</div>
