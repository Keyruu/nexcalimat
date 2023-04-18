<script lang="ts">
	import { GetAccountsStore } from '$houdini';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import UserCard from '$lib/components/storeLogin/UserCard.svelte';
	import { AlertType } from '$lib/types/AlertType';
	import { _ } from 'svelte-i18n';

	const accounts = new GetAccountsStore();
	accounts.fetch();
</script>

<div class="accounts-grid py-4">
	{#if !$accounts.fetching}
		{#if $accounts.data?.accounts && $accounts.data?.accounts.data?.length}
			<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
				{#each $accounts.data.accounts.data as account (account?.id)}
					<UserCard account="{account}" />
				{/each}
			</div>
		{:else}
			<Alert type="{AlertType.Error}">{$_('errors.no-data')}</Alert>
		{/if}
	{:else if $accounts.errors}
		<Alert type="{AlertType.Error}">{$_('errors.no-accounts')}</Alert>
	{/if}
</div>

<style lang="scss">
	.accounts-grid {
		grid-area: 2 / 2 / 5 / 5;
	}
</style>
