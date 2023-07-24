<script lang="ts">
	import type { User } from 'oidc-client-ts';
	import { onMount } from 'svelte';
	import { userManager } from './userManager';

	let user: User | null = null;

	onMount(async () => {
		user = await userManager.getUser();
	});
</script>

{#if user}
	<h1>logged in {user.profile.preferred_username}</h1>
{:else}
	<h1>not logged in</h1>
{/if}
