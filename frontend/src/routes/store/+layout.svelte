<script lang="ts">
	import { page } from '$app/stores';
	import { accountToken } from '$lib/stores/accountStore';
	import { onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';

	const steps = [
		{
			id: 1,
			route: '/store/accounts',
			text: $_('steps.select-account')
		},
		{
			id: 2,
			route: '/store/accounts/[id]',
			text: $_('steps.login')
		},
		{
			id: 3,
			route: '/store/accounts/[id]/products',
			text: $_('steps.choose-product')
		},
		{
			id: 4,
			route: '/store/accounts/[id]/checkout',
			text: $_('steps.buy-product')
		}
	];

	function hasStepPrimary(index: number, currentRoute: string | null): boolean {
		const step = steps.find((s) => s.route === currentRoute);
		return !!step && index <= step.id;
	}

	onDestroy(() => {
		accountToken.set(undefined);
	});
</script>

<div class="p-4 parent-grid h-full w-full">
	<div class="steps-grid py-1 flex items-center justify-center">
		<ul class="steps w-full">
			{#each steps as step (step.id)}
				<li class:step-primary="{hasStepPrimary(step.id, $page.route.id)}" class="step">
					<span class:underline="{step.route === $page.route.id}">{step.text}</span>
				</li>
			{/each}
		</ul>
	</div>

	<slot />
</div>

<style lang="scss">
	.parent-grid {
		display: grid;
		grid-template-columns: repeat(5, 1fr);
		grid-template-rows: repeat(5, 1fr);
		grid-column-gap: 0px;
		grid-row-gap: 0px;
	}
	.steps-grid {
		grid-area: 1 / 2 / 2 / 5;
	}
</style>
