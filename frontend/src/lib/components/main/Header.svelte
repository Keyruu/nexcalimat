<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import { page } from '$app/stores';
	import { account } from '$lib/stores/userManager';
	import { getAccountPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { Avatar } from '@skeletonlabs/skeleton';
	import { _ } from 'svelte-i18n';
</script>

<header class="text-neutral-content flex h-20 flex-row bg-blue-950">
	<div class="flex flex-1 items-center">
		{#if $page.route.id?.startsWith('/store/accounts/') || $page.route.id === '/store/leaderboard'}
			<button class="btn-icon variant-ghost ml-2" on:click="{() => history.back()}">
				<Icon icon="fa-solid:arrow-left" />
			</button>
		{/if}
		<a class="btn variant-ghost ml-2" href="{$page.route.id?.startsWith('/store') ? '' : `${base}/`}">
			<img class="w-6" src="{base}/img/exentra_logo.png" alt="excalimat" />
			<h2 class="text-center align-middle text-2xl font-bold lowercase text-white">&nbsp;nexcalimat</h2>
		</a>
	</div>
	<div class="mr-4 flex flex-none items-center">
		<ul class="flex flex-row space-x-1 p-0">
			<!-- check if the path starts with /store -->
			{#if $page.route.id?.startsWith('/store')}
				{#if $page.route.id === '/store/accounts'}
					<li>
						<a class="btn variant-ghost" href="{base}/store/leaderboard"
							><Icon icon="fa6-solid:ranking-star" />&nbsp;
							{$_('header.leaderboard')}</a
						>
					</li>
				{/if}

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
			{:else if $page.route.id?.startsWith('/admin')}
				{#if $account?.name}
					<div class="flex flex-row">
						<button class="btn variant-ghost mr-2" on:click="{() => goto('/admin/set-pin')}"
							>{$_('header.setpin')}</button
						>
						<Avatar
							src="{getAccountPicture($account)}"
							width="w-12"
							initials="{$account?.name
								.split(' ')
								.map((n) => n[0])
								.join('')}"
							rounded="rounded-full"
						/>
					</div>
				{/if}
				<!-- <li>
					<a class="btn variant-filled font-semibold" href="{base}/signup">{$_('header.signup')}</a>
				</li> -->
			{/if}
		</ul>
	</div>
</header>
