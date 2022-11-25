<script lang="ts">
	import Icon from '@iconify/svelte';
</script>

<div class="flex flex-1 flex-col items-center justify-center text-center">
	<h1 class="mb-12 text-5xl font-medium leading-tight">From knowledge to Kühlschrank</h1>
	<div class="flex items-center gap-2">
		<a href="/store/users" class="btn-primary btn-wide btn my-2 gap-2 normal-case">
			<Icon icon="mdi:store" style="font-size: 24px" />
			Store Login</a
		>
		<a href="/admin/login" class="btn-primary btn-wide btn my-2 gap-2 normal-case">
			<Icon icon="mdi:account-wrench" style="font-size: 24px" />
			Admin Login</a
		>
	</div>
</div>
