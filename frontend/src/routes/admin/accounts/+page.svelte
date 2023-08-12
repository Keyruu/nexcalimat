<script lang="ts">
	import Accounts from '$lib/components/admin/Accounts.svelte';
	import SearchField from '$lib/components/admin/SearchField.svelte';
	import {
		AccountsDocument,
		DeletedAccountsDocument,
		DirectionPojo,
		type AccountsQuery,
		type AccountsQueryVariables,
		type DeletedAccountsQuery,
		type DeletedAccountsQueryVariables,
		type SortPojoInput
	} from '$lib/generated/graphql';
	import Icon from '@iconify/svelte';
	import { SlideToggle } from '@skeletonlabs/skeleton';
	import { queryStore, type OperationResultStore, type QueryArgs } from '@urql/svelte';
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
	let accounts: OperationResultStore<AccountsQuery>;
	let deletedAccounts: OperationResultStore<DeletedAccountsQuery>;
	let accountsQueryOptions: QueryArgs<AccountsQuery, AccountsQueryVariables>;
	let deletedAccountsQueryOptions: QueryArgs<DeletedAccountsQuery, DeletedAccountsQueryVariables>;

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

	$: if (archived) {
		deletedAccountsQueryOptions = {
			client,
			query: DeletedAccountsDocument,
			variables: {
				sort
			}
		};
		deletedAccounts = queryStore<DeletedAccountsQuery, DeletedAccountsQueryVariables>(deletedAccountsQueryOptions);
	} else {
		accountsQueryOptions = {
			client,
			query: AccountsDocument,
			variables: {
				sort,
				searchByName: searchByName === '' ? undefined : searchByName
			}
		};
		accounts = queryStore<AccountsQuery, AccountsQueryVariables>(accountsQueryOptions);
	}

	function refetch() {
		if (archived) {
			deletedAccounts = queryStore<DeletedAccountsQuery, DeletedAccountsQueryVariables>({
				...deletedAccountsQueryOptions,
				requestPolicy: 'network-only'
			});
		} else {
			accounts = queryStore<AccountsQuery, AccountsQueryVariables>({
				...accountsQueryOptions,
				requestPolicy: 'network-only'
			});
		}
	}
</script>

<div class="flex flex-col">
	<div class="flex flex-row items-center">
		<SearchField on:search="{(search) => (searchByName = search.detail)}" />
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
		<SlideToggle name="archived" bind:checked="{archived}" class="mr-4" />
	</div>
	{#if archived}
		{#if $deletedAccounts.data?.deletedAccounts?.data && $deletedAccounts.data.deletedAccounts.data.length > 0}
			<Accounts accounts="{$deletedAccounts.data.deletedAccounts.data}" on:refetch="{refetch}" deletionMode />
		{:else if $accounts.fetching === false}
			<p>{$_('admin.no-account-found')}</p>
		{/if}
	{:else if $accounts.data?.accounts?.data && $accounts.data.accounts.data.length > 0}
		<Accounts accounts="{$accounts.data.accounts.data}" on:refetch="{refetch}" adminMode />
	{:else if $accounts.fetching === false}
		<p>{$_('admin.no-account-found')}</p>
	{/if}
</div>
