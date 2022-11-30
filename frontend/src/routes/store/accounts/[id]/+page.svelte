<script lang="ts">
	import { page } from '$app/stores';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import { ACCOUNT_BY_ID } from '$lib/graphql/ACCOUNT_BY_ID';
	import type { AccountByIdQuery, AccountByIdQueryVariables, PinLoginInput } from '$lib/graphql/generated/graphql';
	import { loggedInAccount } from '$lib/stores/accountStore';
	import { AlertType } from '$lib/types/AlertType';
	import { getImageUrl } from '$lib/utils/account_utils';
	import { query } from 'svelte-apollo';
	import { fade } from 'svelte/transition';
	import { _ } from 'svelte-i18n';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	const account = query<AccountByIdQuery, AccountByIdQueryVariables>(ACCOUNT_BY_ID, {
		variables: {
			id: Number($page.params.id)
		}
	});

	async function handleSubmit() {
		const body: PinLoginInput = { id: Number($page.params.id), pin };
		console.log('submit was pressed', body);
		const res = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/v1/pin/login`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(body)
		});

		if (res.status === 200) {
			const json = await res.json();
			const account = json.data;
			loggedInAccount.set(account);
			triggerSuccess();
		} else {
			loggedInAccount.set(null);
			triggerMiss();
		}
	}
</script>

<div class="py-4">
	{#if !$account.loading}
		{#if $account.data?.account}
			<div class="text-center">
				<div class="avatar">
					<div class="w-24 rounded-full">
						<img alt="account" src="{`${getImageUrl($account.data.account)}`}" />
					</div>
				</div>
				<h1 class="mt-5 mb-8 text-3xl font-medium">{$account.data.account.name}</h1>

				<Keypad class="mt-4" bind:value="{pin}" on:submit="{handleSubmit}" bind:triggerSuccess bind:triggerMiss />
			</div>

			{#if $loggedInAccount}
				<p class="mt-6" transition:fade>Successfully logged in {JSON.stringify($loggedInAccount)}</p>
			{/if}
		{:else}
			<Alert type="{AlertType.Error}">{$_('errors.account-not-found')}</Alert>
		{/if}
	{:else if $account.error}
		<Alert type="{AlertType.Error}">{$_('errors.no-account')}</Alert>
	{/if}
</div>
