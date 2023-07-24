<script lang="ts">
	import { base } from '$app/paths';
	import Icon from '@iconify/svelte';
	import { _ } from 'svelte-i18n';
</script>

<div class="flex flex-1 flex-col items-center justify-center text-center">
	<div
		class="card card-hover variant-glass-surface flex flex-col items-center justify-center text-center shadow-xl hover:variant-ghost-surface sm:p-10 lg:p-20"
	>
		<h1 class="mb-12 text-5xl font-medium leading-tight sm:w-[36rem] lg:w-auto">
			From knowledge to <strong class="bg-gradient-to-br from-sky-400 to-blue-800 bg-clip-text text-transparent"
				>Kühlschrank</strong
			>
		</h1>
		<div class="flex items-center gap-2">
			<a href="{base}/store/accounts" class="btn btn-lg variant-ghost-surface my-2 gap-2 normal-case hover:scale-105">
				<Icon icon="mdi:store" style="font-size: 24px" />
				{$_('homepage.store-login')}</a
			>
			<a href="{base}/admin" class="btn btn-lg variant-ghost-surface my-2 gap-2 normal-case hover:scale-105">
				<Icon icon="mdi:account-wrench" style="font-size: 24px" />
				{$_('homepage.admin-login')}</a
			>
		</div>
	</div>
</div>
