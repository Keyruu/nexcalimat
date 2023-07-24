<script lang="ts">
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { get } from 'svelte/store';
	import { userManager } from './userManager';

	onMount(async () => {
		const user = await userManager.getUser();
		if (!user && get(page).route.id !== '/admin/login/callback') await userManager.signinRedirect();
	});
</script>

<slot />
