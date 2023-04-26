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
	import { Avatar } from '@skeletonlabs/skeleton';
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
			if (result.data?.pinLogin) {
				const token = result.data.pinLogin;
				console.log(token);
				localStorage.setItem('authHeader', `PIN ${token}`);
				accountToken.set(token!);
				triggerSuccess();
				goToFunctionPage();
			} else if (result.error) {
				triggerMiss();
			}
		});
	}

	function goToFunctionPage() {
		goto(`${base}/store/accounts/${$page.params.id}/products`);
	}
</script>

<div class="flex items-center justify-center">
	{#if !$account.fetching}
		{#if $account.data?.account}
			<div class="text-center">
				<div class="variant-glass-surface rounded-2xl p-20 shadow-xl">
					<Avatar
						initials="{$account.data?.account?.name
							?.split(' ')
							.map((n) => n[0])
							.join('')}"
						background="variant-filled-surface"
						class="mx-auto h-24 w-24 border-4 shadow-md border-surface-300-600-token"
					/>
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
</style>
