<script lang="ts">
	import UserCard from '$lib/components/storeLogin/UserCard.svelte';
	import {
		AdminAccountsDocument,
		DirectionPojo,
		type AdminAccountsQuery,
		type AdminAccountsQueryVariables,
		type SortPojoInput
	} from '$lib/generated/graphql';
	import Icon from '@iconify/svelte';
	import { SlideToggle } from '@skeletonlabs/skeleton';
	import { queryStore } from '@urql/svelte';
	import { _ } from 'svelte-i18n';
	import { client } from '../../../urqlClient';

	let boundSearchByName = '';
	let searchByName = '';
	let column = 'name';
	let direction: DirectionPojo = DirectionPojo.Ascending;
	let sort: SortPojoInput = {
		columns: [{ name: column, direction }]
	};
	let archived = false;

	function getSort(columnName: string, directionPojo: DirectionPojo): SortPojoInput {
		return {
			columns: [{ name: columnName, direction: directionPojo }]
		};
	}

	function sortByName() {
		column = 'name';
		sort = getSort(column, direction);
	}

	function sortByBalance() {
		column = 'balance';
		sort = getSort(column, direction);
	}

	function toggleDirection() {
		direction = direction === DirectionPojo.Ascending ? DirectionPojo.Descending : DirectionPojo.Ascending;
		sort = getSort(column, direction);
	}

	function search() {
		searchByName = boundSearchByName;
	}

	$: accounts = queryStore<AdminAccountsQuery, AdminAccountsQueryVariables>({
		client,
		query: AdminAccountsDocument,
		variables: {
			sort,
			searchByName: searchByName === '' ? undefined : searchByName
		}
	});
</script>

<div class="flex flex-col">
	<div class="flex flex-row items-center">
		<form on:submit="{search}" class="input-group input-group-divider grid-cols-[auto_1fr_auto] w-80 m-4">
			<div class="input-group-shim"><Icon icon="fa-solid:search" /></div>
			<input type="search" placeholder="{$_('admin.search')}" bind:value="{boundSearchByName}" />
			<button class="variant-filled-secondary"><Icon icon="fa-solid:arrow-right" /></button>
		</form>
		<span class="divider-vertical h-8 ml-6 mr-6"></span>
		<p>{$_('admin.sort')}:</p>
		<div class="btn-group variant-filled m-4">
			<button
				on:click="{sortByName}"
				disabled="{column === 'name'}"
				class="{column === 'name' ? 'variant-soft' : 'variant-filled'}">{$_('admin.name')}</button
			>
			<button
				on:click="{sortByBalance}"
				disabled="{column === 'balance'}"
				class="{column === 'balance' ? 'variant-soft' : 'variant-filled'}">{$_('admin.balance')}</button
			>
			<button on:click="{toggleDirection}">
				<Icon icon="{direction === DirectionPojo.Ascending ? 'fa-solid:arrow-up' : 'fa-solid:arrow-down'}" />
			</button>
		</div>
		<span class="divider-vertical h-8 ml-6 mr-6"></span>
		<p>{$_('admin.archived')}:&nbsp;</p>
		<SlideToggle name="archived" bind:value="{archived}" class="mr-4" />
	</div>
	{#if $accounts.data?.accounts?.data && $accounts.data.accounts.data.length > 0}
		<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6 m-4">
			{#each $accounts.data.accounts.data as account}
				{#if account}
					<div class="flex justify-center items-center">
						<UserCard {account} adminMode />
					</div>
				{/if}
			{/each}
		</div>
	{:else if $accounts.fetching === false}
		<p>{$_('admin.no-account-found')}</p>
	{/if}
</div>
