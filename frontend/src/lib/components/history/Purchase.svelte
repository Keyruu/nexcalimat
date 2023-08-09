<script lang="ts">
	import type { Purchase } from '$lib/generated/graphql';
	import { stringFormatDate } from '$lib/utils/formatDate';
	import { numberCentToEuro } from '$lib/utils/formatEuro';
	import { getProductPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { createEventDispatcher } from 'svelte';

	export let purchase: Purchase;

	const dispatch = createEventDispatcher();
</script>

<div>
	<div class="variant-ghost-surface m-4 flex flex-row items-center justify-center rounded-xl p-4 relative">
		{#if purchase.product}
			<div class="flex flex-col">
				<img class="h-40" src="{getProductPicture(purchase.product)}" alt="{purchase.product.name}" />
			</div>
			<div class="m-4 flex flex-1 flex-col items-center justify-center">
				<h3 class="font-bold">Product:</h3>
				<h3>{purchase.product.name}</h3>
			</div>
			<div class="m-4 flex flex-col items-center justify-center">
				<h3 class="font-bold">Price:</h3>
				<h3>{numberCentToEuro(purchase.paidPrice)}</h3>
			</div>
			{#if purchase.createdAt}
				<div class="mr-6 flex flex-col items-center justify-center">
					<h3 class="font-bold">Bought at:</h3>
					<h3>{stringFormatDate(purchase.createdAt)}</h3>
				</div>
				{#if Date.now() - Date.parse(purchase.createdAt) < 5 * 60 * 1000}
					<button
						on:click="{() => dispatch('refund', { id: purchase.id })}"
						class="btn-icon variant-filled-error absolute -right-4 -top-4"
					>
						<Icon class="" icon="fa-solid:undo-alt" />
					</button>
				{/if}
			{/if}
		{/if}
	</div>
</div>
