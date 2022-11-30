<script lang="ts">
	import { page } from '$app/stores';
	import { loggedInAccount } from '$lib/stores/accountStore';
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

	function getStepClasses(index: number, currentRoute: string | null) {
		const step = steps.find((s) => s.route === currentRoute);
		return step && index <= step.id ? 'step-primary' : '';
	}

	onDestroy(() => {
		loggedInAccount.set(null);
	});
</script>

<div class="p-4">
	<div class="sticky top-0 z-10 grid bg-base-100 py-1">
		<ul class="steps">
			{#each steps as step (step.id)}
				<li class="{`step ${getStepClasses(step.id, $page.route.id)}`}">
					{step.text}
				</li>
			{/each}
		</ul>
	</div>

	<slot />
</div>
