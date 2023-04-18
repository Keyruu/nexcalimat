<script lang="ts">
	import { page } from '$app/stores';
	import { signIn } from '@auth/sveltekit/client';

	if (!$page.data.session) {
		signIn('azure-ad');
	} else {
	}
</script>

Admin Login
{#if $page.data.session?.user?.image}
	<span style="background-image: url('{$page.data.session.user.image}')" class="avatar"></span>
{/if}
<span class="signedInText">
	<small>Signed in as</small><br />
	<strong>{$page.data.session?.user?.name ?? 'User'}</strong>
</span>
