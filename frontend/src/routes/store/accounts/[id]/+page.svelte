<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import Alert from '$lib/components/alerts/Alert.svelte';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import { ACCOUNT_BY_ID } from '$lib/graphql/ACCOUNT_BY_ID';
	import type {
		AccountByIdQuery,
		AccountByIdQueryVariables,
		PinLoginInput,
		PinLoginQuery,
		PinLoginQueryVariables
	} from '$lib/graphql/generated/graphql';
	import { PIN_LOGIN } from '$lib/graphql/PIN_LOGIN';
	import { accountToken } from '$lib/stores/accountStore';
	import { AlertType } from '$lib/types/AlertType';
	import { getImageUrl } from '$lib/utils/accountUtils';
	import { getClient, query } from 'svelte-apollo';
	import { _ } from 'svelte-i18n';

	let pin: string;

	let triggerSuccess: () => void;
	let triggerMiss: () => void;

	const account = query<AccountByIdQuery, AccountByIdQueryVariables>(ACCOUNT_BY_ID, {
		variables: {
			id: Number($page.params.id)
		}
	});
	const client = getClient();

	function handleSubmit() {
		const login: PinLoginInput = { id: Number($page.params.id), pin };
		const pinLogin = client.query<PinLoginQuery, PinLoginQueryVariables>({ query: PIN_LOGIN, variables: { login } });
		console.log('submit was pressed', login);

		pinLogin
			.then(({ data }) => {
				const token = data.pinLogin;
				accountToken.set(token);
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
	{#if !$account.loading}
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
	{:else if $account.error}
		<Alert type="{AlertType.Error}">{$_('errors.no-account')}</Alert>
	{/if}
</div>

<style lang="scss">
	.keypad-grid {
		grid-area: 2 / 2 / 5 / 5;
	}
</style>
