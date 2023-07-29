<script lang="ts">
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';
	import { userManager } from '../../../../lib/stores/userManager';

	onMount(async () => {
		await userManager.signinRedirectCallback();
		await userManager.getUser();
		goto('/admin');
	});
</script>
