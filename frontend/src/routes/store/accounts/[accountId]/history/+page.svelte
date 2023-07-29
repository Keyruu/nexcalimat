<script lang="ts">
	import Purchase from '$lib/components/history/Purchase.svelte';
	import {
		MyPurchasesDocument,
		RefundDocument,
		type MyPurchasesQuery,
		type RefundMutation,
		type RefundMutationVariables
	} from '$lib/generated/graphql';
	import { error } from '$lib/utils/storeError';
	import { toastStore } from '@skeletonlabs/skeleton';
	import { getContextClient, mutationStore, queryStore } from '@urql/svelte';
	import { onDestroy, onMount } from 'svelte';
	import type { Unsubscriber } from 'svelte/store';

	const client = getContextClient();
	$: purchases = queryStore<MyPurchasesQuery>({
		client,
		query: MyPurchasesDocument
	});

	function refresh() {
		queryStore<MyPurchasesQuery>({
			client,
			query: MyPurchasesDocument,
			requestPolicy: 'network-only'
		});
	}

	const refund = (id: number) =>
		mutationStore<RefundMutation, RefundMutationVariables>({
			client,
			query: RefundDocument,
			variables: {
				id: id
			}
		});

	let unsubscribeRefund: Unsubscriber;

	async function submitRefund(id: number | null | undefined) {
		if (!id) {
			return;
		}

		unsubscribeRefund = refund(id).subscribe((result) => {
			if (result.fetching === false && result.data?.refundPurchase) {
				toastStore.trigger({
					message: 'Purchase was refunded.',
					classes: 'text-green-300',
					background: 'variant-glass-success'
				});
				refresh();
			} else if (result.error) {
				toastStore.trigger({
					message: 'Something went wrong.',
					classes: 'text-red-300',
					background: 'variant-glass-error'
				});
			}
		});
	}

	let unsubscribe: Unsubscriber;

	onMount(() => {
		unsubscribe = purchases.subscribe((result) => {
			if (result.error) {
				error(result.error);
			}
		});
	});

	onDestroy(() => {
		unsubscribe();
		if (unsubscribeRefund) {
			unsubscribeRefund();
		}
	});
</script>

<h2 class="font-bold">History:</h2>
<div class="flex flex-col justify-center">
	{#if $purchases.fetching}
		<p>Loading...</p>
	{:else if $purchases.error}
		<p>Error: {$purchases.error.message}</p>
	{:else if !$purchases.data?.myPurchases?.data?.length}
		<p>No purchases</p>
	{:else}
		{#each $purchases.data.myPurchases.data as purchase (purchase?.id)}
			{#if purchase && purchase.id}
				{#if purchase}
					<Purchase on:refund="{() => submitRefund(purchase?.id)}" purchase="{purchase}" />
				{/if}
			{/if}
		{/each}
	{/if}
</div>
