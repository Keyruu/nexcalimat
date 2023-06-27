<script lang="ts">
	import { base } from '$app/paths';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import UserCard from '$lib/components/storeLogin/UserCard.svelte';
	import { AccountsDocument, type AccountsQuery } from '$lib/generated/graphql';
	import { authHeader } from '$lib/stores/authHeader';
	import { AlertType } from '$lib/types/AlertType';
	import { getContextClient, queryStore } from '@urql/svelte';
	import { _ } from 'svelte-i18n';

	localStorage.setItem('authHeader', '');
	authHeader.set(undefined);

	const accounts = queryStore<AccountsQuery>({
		client: getContextClient(),
		query: AccountsDocument
	});
</script>

<div class="w-full py-4">
	{#if !$accounts.fetching}
		{#if $accounts.data?.accounts && $accounts.data?.accounts.data?.length}
			<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
				{#each $accounts.data.accounts.data as account (account?.id)}
					{#if account}
						<a href="{base}/store/accounts/{account.id}" class="unstyled flex items-center justify-center">
							<UserCard account="{account}" />
						</a>
					{/if}
				{/each}
			</div>
		{:else}
			<Alert type="{AlertType.Error}">{$_('errors.no-data')}</Alert>
		{/if}
	{:else if $accounts.error}
		<Alert type="{AlertType.Error}">{$_('errors.no-accounts')}</Alert>
	{/if}
</div>

<style lang="scss">
</style>
