<script lang="ts">
	import { PUBLIC_STORAGE_URL } from '$env/static/public';
	import type { Product } from '$lib/generated/graphql';

	export let product: Product;
</script>

<div class="m-4">
	<div class="card-compact card h-72 w-64 cursor-pointer bg-neutral shadow-md hover:bg-neutral-focus hover:shadow-xl">
		<figure class="m-4">
			<img
				class="max-h-full max-w-full"
				src="{`${PUBLIC_STORAGE_URL}product/${product.picture}`}"
				alt="{product.name}"
			/>
		</figure>
		<div class="card-body items-center text-center">
			<h2 class="card-title font-bold">
				{product.name}
			</h2>
			{#if product.price && product.type}
				<div>
					<div class="badge-secondary badge font-bold">{(product.price / 100).toFixed(2)} €</div>
					<div
						class="
					badge
					font-bold
					{product.type === 'HOT_DRINK' ? 'bg-red-700' : 'bg-blue-700'}"
					>
						{product.type === 'HOT_DRINK' ? 'hot' : 'cold'}
					</div>
				</div>
			{/if}
		</div>
	</div>
</div>
