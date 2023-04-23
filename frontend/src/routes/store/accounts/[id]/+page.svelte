<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import {
		AccountByIdDocument,
		PinLoginDocument,
		type AccountByIdQuery,
		type AccountByIdQueryVariables,
		type PinLoginQuery,
		type PinLoginQueryVariables
	} from '$lib/generated/graphql';
	import { accountToken } from '$lib/stores/accountStore';
	import { AlertType } from '$lib/types/AlertType';
	import { getImageUrl } from '$lib/utils/accountUtils';
	import { getContextClient, queryStore } from '@urql/svelte';
	import { _ } from 'svelte-i18n';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	const urqlClient = getContextClient();

	const account = queryStore<AccountByIdQuery, AccountByIdQueryVariables>({
		client: urqlClient,
		query: AccountByIdDocument,
		variables: {
			id: Number($page.params.id)
		}
	});

	async function handleSubmit() {
		const pinStore = queryStore<PinLoginQuery, PinLoginQueryVariables>({
			client: urqlClient,
			query: PinLoginDocument,
			variables: {
				login: {
					id: Number($page.params.id),
					pin
				}
			}
		});

		console.log('submit was pressed', pin);

		pinStore.subscribe((result) => {
			if (result.data) {
				const token = result.data.pinLogin;
				console.log(token);
				localStorage.setItem('authHeader', `PIN ${token}`);
				accountToken.set(token!);
				triggerSuccess();
				goToFunctionPage();
			}
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
					<h1 class="mb-8 mt-5 text-3xl font-medium">{$account.data.account.name}</h1>

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
	{:else if $account.error}
		<Alert type="{AlertType.Error}">{$_('errors.no-account')}</Alert>
	{/if}
</div>

<style lang="scss">
	.keypad-grid {
		grid-area: 2 / 2 / 5 / 5;
	}
</style>
