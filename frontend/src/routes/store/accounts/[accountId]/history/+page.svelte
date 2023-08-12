<script lang="ts">
	import Purchase from '$lib/components/history/Purchase.svelte';
	import {
		MyPurchasesDocument,
		RefundDocument,
		type MyPurchasesQuery,
		type RefundMutation,
		type RefundMutationVariables
	} from '$lib/generated/graphql';
	import { handleError } from '$lib/utils/storeError';
	import { toastError, toastSuccess } from '$lib/utils/toastUtils';
	import { toastStore } from '@skeletonlabs/skeleton';
	import { mutationStore, queryStore } from '@urql/svelte';
	import { onDestroy, onMount } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';
	import { client } from '../../../../../urqlClient';

	const queryOptions = {
		client,
		query: MyPurchasesDocument
	};

	$: purchases = queryStore<MyPurchasesQuery>(queryOptions);

	function refresh() {
		purchases = queryStore<MyPurchasesQuery>({
			...queryOptions,
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
				toastStore.trigger(toastSuccess($_('toast.refund.success')));
				refresh();
			} else if (result.error) {
				toastStore.trigger(toastError($_('toast.generic.error')));
			}
		});
	}

	let unsubscribe: Unsubscriber;

	onMount(() => {
		unsubscribe = purchases.subscribe((result) => {
			if (result.error) {
				handleError(result.error);
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
					<Purchase on:refund="{() => submitRefund(purchase?.id)}" {purchase} />
				{/if}
			{/if}
		{/each}
	{/if}
</div>
