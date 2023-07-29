<script lang="ts">
	import { oidcUser } from '$lib/stores/userManager';
</script>

{#if $oidcUser}
	<h1>logged in {$oidcUser.profile.preferred_username} + {$oidcUser.expired}</h1>
{:else}
	<h1>not logged in</h1>
{/if}
