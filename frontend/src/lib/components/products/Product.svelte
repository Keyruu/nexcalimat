<script lang="ts">
	import { base } from '$app/paths';
	import { PUBLIC_STORAGE_URL } from '$env/static/public';
	import type { ProductWithFavorite } from '$lib/generated/graphql';

	export let product: ProductWithFavorite;
</script>

<div class="m-4">
	<div
		class="sm:w-50 card card-hover variant-glass-surface cursor-pointer shadow-md hover:variant-ghost-surface hover:shadow-xl sm:h-64 lg:h-72 lg:w-64"
	>
		<figure class="m-4 flex items-center justify-center py-4 sm:h-36 lg:h-44">
			<img
				class="max-h-full max-w-full"
				src="{product.picture ? `${PUBLIC_STORAGE_URL}/product/${product.picture}` : `${base}/img/default_bottle.png`}"
				alt="{product.name}"
			/>
		</figure>
		<div class="items-center text-center">
			<h2 class="unstyled my-2 font-bold sm:text-3xl lg:text-4xl">
				{product.name}
			</h2>
			<div>
				{#if product.isFavorite}
					<div class="badge bg-pink-500">♥</div>
				{/if}
				{#if product.price && product.type}
					<div class="badge variant-filled-surface font-bold">{(product.price / 100).toFixed(2)} €</div>
					<div
						class="
					badge
					font-bold
					{product.type === 'HOT_DRINK' ? 'bg-red-700' : 'bg-blue-700'}"
					>
						{product.type === 'HOT_DRINK' ? 'hot' : 'cold'}
					</div>
				{/if}
			</div>
		</div>
	</div>
</div>
