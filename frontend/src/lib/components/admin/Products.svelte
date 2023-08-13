<script lang="ts">
	import type { Product } from '$lib/generated/graphql';
	import { createEventDispatcher } from 'svelte';
	import ProductComponent from '../products/Product.svelte';
	import Grid from './Grid.svelte';

	export let products: (Product | null)[];

	const dispatch = createEventDispatcher();
</script>

<Grid>
	{#each products as product}
		{#if product}
			<ProductComponent {product} adminMode on:refetch="{() => dispatch('refetch')}" />
		{/if}
	{/each}
</Grid>
