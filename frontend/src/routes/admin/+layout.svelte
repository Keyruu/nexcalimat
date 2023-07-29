<script lang="ts">
	import { page } from '$app/stores';
	import { handleMyAccount, setUser, userManager } from '$lib/stores/userManager';
	import { get } from 'svelte/store';

	handleUser();

	async function handleUser() {
		const user = await userManager.getUser();
		if (!user && get(page).route.id !== '/admin/login/callback') {
			await userManager.signinRedirect();
		} else if (user && user.expired) {
			userManager.signinSilent();
		} else if (user) {
			setUser(user);

			handleMyAccount();
		}
	}
</script>

<slot />
