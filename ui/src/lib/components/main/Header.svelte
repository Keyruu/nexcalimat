<script lang="ts">
	import Icon from '@iconify/svelte';
</script>

<header class="navbar bg-neutral text-neutral-content">
	<div class="flex-1">
		<a class="btn-ghost btn" href="/">
			<img class="w-36" src="/img/excalimat_logo.png" alt="excalimat" />
		</a>
	</div>
	<div class="flex-none">
		<ul class="menu menu-horizontal p-0">
			<li>
				<a href="/statistics">
					<Icon icon="mdi:chart-bar" />
					Statistics</a
				>
			</li>
		</ul>
	</div>
</header>
