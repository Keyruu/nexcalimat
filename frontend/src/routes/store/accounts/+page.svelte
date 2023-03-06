<script lang="ts">
	import Alert from '$lib/components/alerts/Alert.svelte';
	import UserCard from '$lib/components/storeLogin/UserCard.svelte';
	import type { AccountsQuery, AccountsQueryVariables } from '$lib/graphql/generated/graphql';
	import { GET_ACCOUNTS } from '$lib/graphql/GET_ACCOUNTS';
	import { AlertType } from '$lib/types/AlertType.js';
	import { query } from 'svelte-apollo';
	import { _ } from 'svelte-i18n';

	const accounts = query<AccountsQuery, AccountsQueryVariables>(GET_ACCOUNTS);
</script>

<div class="accounts-grid py-4">
	{#if !$accounts.loading}
		{#if $accounts.data?.accounts && $accounts.data?.accounts.data?.length}
			<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
				{#each $accounts.data.accounts.data as account (account?.id)}
					<UserCard account="{account}" />
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
	.accounts-grid {
		grid-area: 2 / 2 / 5 / 5;
	}
</style>
