<script lang="ts">
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import Icon from '@iconify/svelte';
	import { _ } from 'svelte-i18n';
</script>

<header class="text-neutral-content flex h-20 flex-row bg-blue-950">
	<div class="flex flex-1 items-center">
		{#if $page.route.id?.startsWith('/store/accounts/')}
			<button class="variant-ghost btn-icon ml-2" on:click="{() => history.back()}">
				<Icon icon="fa-solid:arrow-left" />
			</button>
		{/if}
		<a class="variant-ghost btn ml-2" href="{$page.route.id?.startsWith('/store') ? '' : `${base}/`}">
			<img class="w-6" src="{base}/img/exentra_logo.png" alt="excalimat" />
			<h2 class="text-center align-middle text-2xl font-bold lowercase text-white">&nbsp;nexcalimat</h2>
		</a>
	</div>
	<div class="mr-4 flex flex-none items-center">
		<ul class="menu menu-horizontal p-0">
			<!-- check if the path starts with /store -->
			{#if $page.route.id?.startsWith('/store')}
				{#if $page.route.id?.includes('/products') || $page.route.id?.includes('/history')}
					<li>
						<a
							class="btn-icon font-semibold {$page.route.id?.includes('/history')
								? 'variant-filled'
								: 'variant-ringed'}"
							href="{base}/store/accounts/{$page.params.accountId}/history"><Icon icon="fa-solid:history" /></a
						>
					</li>
				{/if}
			{:else}
				<li>
					<a class="btn variant-filled font-semibold" href="{base}/signup">{$_('header.signup')}</a>
				</li>
			{/if}
		</ul>
	</div>
</header>
