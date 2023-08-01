<script lang="ts">
	import { AccountsDocument, type AccountsQuery } from '$lib/generated/graphql';
	import { oidcUser } from '$lib/stores/userManager';
	import { queryStore, type AnyVariables, type OperationResultStore } from '@urql/svelte';
	import { client } from '../../urqlClient';

	let accounts: OperationResultStore<AccountsQuery, AnyVariables>;

	$: if ($oidcUser) {
		accounts = queryStore<AccountsQuery>({ client, query: AccountsDocument });
	}
</script>

{#if $oidcUser}
	<h1>logged in {$oidcUser.profile.preferred_username} + {$oidcUser.expired}</h1>
	{#if $accounts.data?.accounts?.data}
		<p>{$accounts.data?.accounts?.data[0]?.email}</p>
	{/if}
{:else}
	<h1>not logged in</h1>
{/if}
