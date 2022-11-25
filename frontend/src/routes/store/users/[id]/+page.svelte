<script lang="ts">
	import { page } from '$app/stores';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import { loggedInAccount } from '$lib/stores/userstore';
	import type { Account } from '$lib/types/Account';
	import { AlertType } from '$lib/types/AlertType';
	import type { PinInput } from '$lib/types/PinInput';
	import { getImageUrl } from '$lib/utils/account_utils';
	import { fade } from 'svelte/transition';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	async function loadUser(): Promise<Account> {
		const res = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/v1/account/${$page.params.id}`);
		if (res.status !== 200) {
			throw new Error('User not found');
		}
		const json = await res.json();
		return json.data;
	}

	async function handleSubmit() {
		const body: PinInput = { account_id: Number($page.params.id), pin };
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
	{#await loadUser() then account}
		<div class="text-center">
			<div class="avatar">
				<div class="w-24 rounded-full">
					<img alt="account" src="{`${getImageUrl(account)}`}" />
				</div>
			</div>
			<h1 class="text-3xl font-medium mt-5 mb-8">{account.name}</h1>

			<Keypad class="mt-4" bind:value="{pin}" on:submit="{handleSubmit}" bind:triggerSuccess bind:triggerMiss />
		</div>

		{#if $loggedInAccount}
			<p class="mt-6" transition:fade>Successfully logged in {JSON.stringify($loggedInAccount)}</p>
		{/if}
	{:catch}
		<Alert type="{AlertType.Error}">Could not load account!</Alert>
	{/await}
</div>
