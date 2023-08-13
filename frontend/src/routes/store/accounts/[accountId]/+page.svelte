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
	import { authHeader } from '$lib/stores/authHeader';
	import { AlertType } from '$lib/types/AlertType';
	import { getInitials } from '$lib/utils/accountUtils';
	import { getAccountPicture } from '$lib/utils/pictureUtils';
	import { Avatar } from '@skeletonlabs/skeleton';
	import { getContextClient, queryStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	const urqlClient = getContextClient();

	authHeader.set('');

	const account = queryStore<AccountByIdQuery, AccountByIdQueryVariables>({
		client: urqlClient,
		query: AccountByIdDocument,
		variables: {
			id: Number($page.params.accountId)
		}
	});

	const login = () =>
		queryStore<PinLoginQuery, PinLoginQueryVariables>({
			client: urqlClient,
			query: PinLoginDocument,
			variables: {
				login: {
					id: Number($page.params.accountId),
					pin
				}
			}
		});

	let unsubscribeLogin: Unsubscriber;

	async function handleSubmit() {
		console.log('submit was pressed', pin);

		unsubscribeLogin = login().subscribe((result) => {
			if (result.fetching === false && result.data?.pinLogin) {
				const token = result.data.pinLogin;
				console.log(result);
				localStorage.setItem('authHeader', `PIN ${token}`);
				authHeader.set(`PIN ${token}`);
				triggerSuccess();
				goToFunctionPage();
			} else if (result.error) {
				triggerMiss();
			}
		});
	}

	function goToFunctionPage() {
		goto(`${base}/store/accounts/${$page.params.accountId}/products`);
	}

	onDestroy(() => {
		if (unsubscribeLogin) {
			unsubscribeLogin();
		}
	});
</script>

<div class="flex items-center justify-center">
	{#if !$account.fetching}
		{#if $account.data?.account}
			<div class="text-center">
				<div class="variant-glass-surface rounded-2xl p-20 shadow-xl">
					<Avatar
						src="{getAccountPicture($account.data.account)}"
						initials="{getInitials($account.data.account)}"
						background="variant-filled-surface"
						class="mx-auto md:h-24 md:w-24 border-4 shadow-md border-surface-300-600-token"
					/>
					<h1 class="mb-8 mt-5 text-3xl font-medium">{$account.data.account.name}</h1>

					<Keypad class="mt-4" bind:value="{pin}" on:submit="{handleSubmit}" bind:triggerSuccess bind:triggerMiss />
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
</style>
