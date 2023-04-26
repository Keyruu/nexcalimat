<script lang="ts">
	import { base } from '$app/paths';
	import { _ } from 'svelte-i18n';
</script>

<header class="text-neutral-content flex h-20 flex-row bg-blue-950">
	<div class="flex flex-1 items-center">
		<a class="variant-ghost btn ml-2" href="{base}/">
			<img class="w-6" src="{base}/img/exentra_logo.png" alt="excalimat" />
			<h2 class="text-center align-middle text-2xl font-bold lowercase text-white">&nbsp;nexcalimat</h2>
		</a>
	</div>
	<div class="mr-4 flex flex-none items-center">
		<ul class="menu menu-horizontalp-0">
			<li>
				<a class="btn variant-filled font-semibold" href="{base}/signup">{$_('header.signup')}</a>
			</li>
		</ul>
	</div>
</header>
