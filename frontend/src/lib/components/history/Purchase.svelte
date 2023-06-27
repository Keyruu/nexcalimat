<script lang="ts">
	import type { Purchase } from '$lib/generated/graphql';
	import { centToEuro } from '$lib/utils/formatEuro';
	import { getProductPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { getContextClient } from '@urql/svelte';
	import { createEventDispatcher } from 'svelte';

	export let purchase: Purchase;

	const urqlClient = getContextClient();
	const dispatch = createEventDispatcher();
</script>

<div>
	<div class="variant-ghost-surface m-4 flex flex-row items-center justify-center rounded-xl p-4">
		{#if purchase.product}
			<div class="flex flex-col">
				<img class="h-24 w-24" src="{getProductPicture(purchase.product)}" alt="{purchase.product.name}" />
			</div>
			<div class="m-4 flex flex-1 flex-col items-center justify-center">
				<h3 class="font-bold">Product:</h3>
				<h3>{purchase.product.name}</h3>
			</div>
			<div class="mr-6 flex flex-col items-center justify-center">
				<h3 class="font-bold">Price:</h3>
				<h3 use:centToEuro>{purchase.paidPrice}</h3>
			</div>
			{#if purchase.createdAt}
				{#if Date.now() - Date.parse(purchase.createdAt) < 5 * 60 * 1000}
					<div class="relative right-1 mt-[-165px]">
						<button
							on:click="{() => dispatch('refund', { id: purchase.id })}"
							class="btn-icon variant-filled-error absolute"
						>
							<Icon class="" icon="fa-solid:undo-alt" />
						</button>
					</div>
				{/if}
			{/if}
		{/if}
	</div>
</div>
