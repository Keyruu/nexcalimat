<script lang="ts">
	import type { Account } from '$lib/generated/graphql';
	import { createEventDispatcher } from 'svelte';
	import UserCard from '../storeLogin/UserCard.svelte';
	import Grid from './Grid.svelte';

	const dispatch = createEventDispatcher();

	export let accounts: (Account | null)[];
	export let adminMode = false;
	export let deletionMode = false;
</script>

<Grid>
	{#each accounts as account}
		{#if account}
			<div class="flex justify-center items-center">
				<UserCard on:refetch="{() => dispatch('refetch')}" {account} {deletionMode} {adminMode} />
			</div>
		{/if}
	{/each}
</Grid>
