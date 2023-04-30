<script lang="ts">
	import { base } from '$app/paths';
	import { PUBLIC_STORAGE_URL } from '$env/static/public';
	import type { ProductWithFavorite } from '$lib/generated/graphql';
	import { centToEuro } from '$lib/utils/formatEuro';

	export let product: ProductWithFavorite;
</script>

<div class="m-4">
	<div
		class="sm:w-50 card card-hover variant-glass-surface flex cursor-pointer flex-col shadow-md hover:variant-ghost-surface hover:shadow-xl sm:h-72 lg:h-80 lg:w-64"
	>
		<figure class="m-4 mb-0 flex items-center justify-center py-4 sm:h-36 lg:h-44">
			<img
				class="max-h-full max-w-full"
				src="{product.picture ? `${PUBLIC_STORAGE_URL}/product/${product.picture}` : `${base}/img/default_bottle.png`}"
				alt="{product.name}"
			/>
		</figure>
		<div class="flex flex-1 flex-col items-center text-center">
			<div class="flex-1 items-center justify-center">
				<h2 class="unstyled font-bold sm:text-3xl lg:text-4xl">
					{product.name}
				</h2>
			</div>
			<div class="mb-4">
				{#if product.isFavorite}
					<div class="badge bg-pink-500">♥</div>
				{/if}
				{#if product.price && product.type}
					<div class="badge variant-filled-surface font-bold" use:centToEuro>
						{product.price}
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
			</div>
		</div>
	</div>
</div>
