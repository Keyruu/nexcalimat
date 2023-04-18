<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import { AccountByIdStore, type AccountById$input, PinLoginStore } from '$houdini';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import { accountToken } from '$lib/stores/accountStore';
	import { AlertType } from '$lib/types/AlertType';
	import { getImageUrl } from '$lib/utils/accountUtils';
	import { _ } from 'svelte-i18n';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	const account = new AccountByIdStore();
	account.fetch({
		variables: {
			id: Number($page.params.id)
		}
	});

	function handleSubmit() {
		const pinLogin = new PinLoginStore();

		console.log('submit was pressed', pin);

		pinLogin
			.fetch({
				variables: {
					login: {
						id: Number($page.params.id),
						pin
					}
				}
			})
			.then(({ data }) => {
				const token = data?.pinLogin;
				accountToken.set(token!);
				triggerSuccess();
				goToFunctionPage();
			})
			.catch((e) => {
				const errorCodes = e.graphQLErrors.map((gqlError: any) => gqlError.extensions.code);
				console.error('error codes', errorCodes);

				accountToken.set(undefined);
				triggerMiss();
			});
	}

	function goToFunctionPage() {
		goto(`${base}/store/accounts/${$page.params.id}/products`);
	}
</script>

<div class="keypad-grid flex items-center justify-center">
	{#if !$account.fetching}
		{#if $account.data?.account}
			<div class="text-center">
				<div class="rounded-2xl bg-neutral p-20 shadow-xl">
					<div class="avatar">
						<div class="w-24 rounded-full">
							<img alt="account" src="{`${getImageUrl($account.data.account)}`}" />
						</div>
					</div>
					<h1 class="mt-5 mb-8 text-3xl font-medium">{$account.data.account.name}</h1>

					<Keypad
						class="mt-4"
						bind:value="{pin}"
						on:submit="{handleSubmit}"
						bind:triggerSuccess="{triggerSuccess}"
						bind:triggerMiss="{triggerMiss}"
					/>
				</div>
			</div>
		{:else}
			<Alert type="{AlertType.Error}">{$_('errors.account-not-found')}</Alert>
		{/if}
	{:else if $account.errors}
		<Alert type="{AlertType.Error}">{$_('errors.no-account')}</Alert>
	{/if}
</div>

<style lang="scss">
	.keypad-grid {
		grid-area: 2 / 2 / 5 / 5;
	}
</style>
