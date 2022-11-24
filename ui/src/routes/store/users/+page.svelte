<script lang="ts">
	import Alert from '$lib/components/alerts/Alert.svelte';
	import UserCard from '$lib/components/storeLogin/UserCard.svelte';
	import type { Account } from '$lib/types/Account';
	import { AlertType } from '$lib/types/AlertType';

	async function loadUsers(): Promise<Account[]> {
		const res = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/v1/account`);
		if (res.status !== 200) {
			throw new Error('Users not found');
		}
		const json: { data: Account[] } = await res.json();
		return json.data;
	}
</script>

<div class="py-4">
	{#await loadUsers() then accounts}
		{#if accounts && accounts.length}
			<div class="grid grid-cols-1 content-evenly gap-4 md:grid-cols-3 lg:grid-cols-4 2xl:grid-cols-6">
				{#each accounts as account (account.ID)}
					<UserCard account="{account}" />
				{/each}
			</div>
		{:else}
			<Alert type="{AlertType.Error}">No data!</Alert>
		{/if}
	{:catch}
		<Alert type="{AlertType.Error}">Could not load accounts!</Alert>
	{/await}
</div>
