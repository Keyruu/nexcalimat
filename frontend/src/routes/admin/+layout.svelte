<script lang="ts">
	import { page } from '$app/stores';
	import { account, handleMyAccount, isAdmin, setUser, userManager } from '$lib/stores/userManager';
	import { TabAnchor, TabGroup } from '@skeletonlabs/skeleton';
	import { _ } from 'svelte-i18n';
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

<TabGroup justify="justify-center" class="mt-4">
	<TabAnchor href="/admin" selected="{$page.route.id === '/admin'}" class="unstyled"
		><p class="">{$_('admin.my-account')}</p></TabAnchor
	>
	{#if $account && $isAdmin}
		<TabAnchor href="/admin/accounts" selected="{$page.route.id === '/admin/accounts'}" class="unstyled">
			{$_('admin.accounts')}
		</TabAnchor>
		<TabAnchor href="/admin/products" selected="{$page.route.id?.startsWith('/admin/products')}" class="unstyled">
			{$_('admin.products')}
		</TabAnchor>
	{/if}
	<TabAnchor href="/admin/statistics" selected="{$page.route.id === '/admin/statistics'}" class="unstyled">
		{$_('admin.statistics')}
	</TabAnchor>
</TabGroup>
<slot />
