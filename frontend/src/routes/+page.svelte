<script lang="ts">
	import { base } from '$app/paths';
	import Icon from '@iconify/svelte';
	import { _ } from 'svelte-i18n';
</script>

<div class="flex flex-1 flex-col items-center justify-center text-center">
	<div class="card p-20 bg-neutral shadow-xl flex items-center justify-center text-center glow-shadow">
		<h1 class="mb-12 text-5xl font-medium leading-tight">
			From knowledge to <strong class="bg-gradient-to-br to-blue-800 from-sky-400 bg-clip-text text-transparent"
				>Kühlschrank</strong
			>
		</h1>
		<div class="flex items-center gap-2">
			<a href="{base}/store/accounts" class="btn-primary btn-wide btn my-2 gap-2 normal-case">
				<Icon icon="mdi:store" style="font-size: 24px" />
				{$_('homepage.store-login')}</a
			>
			<a href="{base}/admin/login" class="btn-primary btn-wide btn my-2 gap-2 normal-case">
				<Icon icon="mdi:account-wrench" style="font-size: 24px" />
				{$_('homepage.admin-login')}</a
			>
		</div>
	</div>
</div>

<style lang="scss">
	$blue-glow: #5272ff;

	.glow-shadow {
		transition: 0.2s;
		transition-property: box-shadow;
		box-shadow: 0 0 25px $blue-glow;
	}

	.glow-shadow:hover {
		box-shadow: 0 0 5px $blue-glow, 0 0 25px $blue-glow, 0 0 50px $blue-glow, 0 0 100px $blue-glow;
	}
</style>
