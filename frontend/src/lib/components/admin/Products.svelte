<script lang="ts">
	import type { Product } from '$lib/generated/graphql';
	import ProductComponent from '../products/Product.svelte';
	import Grid from './Grid.svelte';

	export let products: (Product | null)[];
</script>

<Grid>
	{#each products as product}
		{#if product}
			<ProductComponent {product} />
		{/if}
	{/each}
</Grid>
