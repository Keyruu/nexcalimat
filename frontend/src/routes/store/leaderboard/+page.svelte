<script lang="ts">
	import Alert from '$lib/components/alerts/Alert.svelte';
	import { LeaderboardDocument, type LeaderboardQuery, type LeaderboardQueryVariables } from '$lib/generated/graphql';
	import { AlertType } from '$lib/types/AlertType';
	import { getInitials } from '$lib/utils/accountUtils';
	import { getAccountPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { Avatar, Paginator } from '@skeletonlabs/skeleton';
	import type { PaginationSettings } from '@skeletonlabs/skeleton/dist/components/Paginator/types';
	import { getContextClient, queryStore } from '@urql/svelte';
	import { onDestroy, onMount } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';

	let unsubscriber: Unsubscriber;

	let page: PaginationSettings = {
		offset: 0,
		limit: 20,
		size: 0,
		amounts: [10, 20]
	};

	$: accountsWithPurchaseCount = queryStore<LeaderboardQuery, LeaderboardQueryVariables>({
		client: getContextClient(),
		query: LeaderboardDocument,
		variables: {
			page: {
				index: page.offset,
				size: page.limit
			}
		}
	});

	onMount(() => {
		unsubscriber = accountsWithPurchaseCount.subscribe((result) => {
			if (result.data?.leaderboard?.total && !page.size) {
				page.size = result.data.leaderboard.total ?? 0;
			}
		});
	});

	onDestroy(() => {
		unsubscriber();
	});

	function getColor(index: number) {
		switch (index) {
			case 0:
				return 'text-[#C9B037]';
			case 1:
				return 'text-[#B4B4B4]';
			case 2:
				return 'text-[#AD8A56]';
			default:
				return '';
		}
	}
</script>

<h1 class="h1 mb-4">{$_('header.leaderboard')}</h1>
{#if $accountsWithPurchaseCount.data?.leaderboard && $accountsWithPurchaseCount.data?.leaderboard.data}
	<ul class="list sm:w-3/5 w-full">
		{#each $accountsWithPurchaseCount.data.leaderboard.data as accountWithCount, index (accountWithCount?.account?.id)}
			{#if accountWithCount && accountWithCount.account}
				<li>
					<div
						class="card card-hover variant-glass-surface flex w-full flex-row items-center p-2 shadow-md hover:variant-ghost-surface hover:shadow-xl relative"
					>
						<Avatar
							src="{getAccountPicture(accountWithCount.account)}"
							initials="{getInitials(accountWithCount.account)}"
							width="w-16 mr-3"
							background="variant-filled-surface"
							rounded="rounded-full"
						/>
						<div class="flex-auto font-semibold">{accountWithCount.account.name}</div>
						<div class="mr-5 font-semibold">{accountWithCount.count}</div>
						{#if index < 3 && page.offset === 0 && accountWithCount.count}
							<Icon class="{`absolute -top-2 -right-2 w-8 h-8 ${getColor(index)}`}" icon="fa6-solid:award" />
						{/if}
					</div>
				</li>
			{/if}
		{/each}
	</ul>
{/if}

{#if page.size}
	<Paginator class="mt-4" controlVariant="variant-glass-surface" bind:settings="{page}" />
{:else if !page.size && !$accountsWithPurchaseCount.fetching}
	<div class="sm:w-3/5 w-full">
		<Alert type="{AlertType.Error}">{$_('errors.no-data')}</Alert>
	</div>
{/if}
