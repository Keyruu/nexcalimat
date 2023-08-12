<script lang="ts">
	import Icon from '@iconify/svelte';
	import { createEventDispatcher } from 'svelte';
	import { _ } from 'svelte-i18n';

	const dispatch = createEventDispatcher();

	let boundSearchByName = '';
</script>

<form
	on:submit="{() => dispatch('search', boundSearchByName)}"
	class="input-group input-group-divider grid-cols-[auto_1fr_auto] w-80 m-4"
>
	<div class="input-group-shim"><Icon icon="fa-solid:search" /></div>
	<input type="search" placeholder="{$_('admin.search')}" bind:value="{boundSearchByName}" />
	<button class="variant-filled-secondary"><Icon icon="fa-solid:arrow-right" /></button>
</form>
