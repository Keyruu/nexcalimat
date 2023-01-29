<script lang="ts">
	import { base } from '$app/paths';
	import { _ } from 'svelte-i18n';
</script>

<header class="navbar bg-neutral text-neutral-content">
	<div class="flex-1">
		<a class="btn-ghost btn" href="{base}/">
			<img class="w-6" src="{base}/img/exentra_logo.png" alt="excalimat" />
			<h1 class="text-center align-middle text-2xl font-bold lowercase text-white">&nbsp;nexcalimat</h1>
		</a>
	</div>
	<div class="flex-none">
		<ul class="menu menu-horizontal p-0">
			<li>
				<a class="font-semibold" href="{base}/signup">{$_('header.signup')}</a>
			</li>
		</ul>
	</div>
</header>
