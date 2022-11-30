<script lang="ts">
	import { page } from '$app/stores';
	import { loggedInAccount } from '$lib/stores/accountStore';
	import { onDestroy } from 'svelte';

	const steps = [
		{
			id: 1,
			route: '/store/accounts',
			text: 'Select account'
		},
		{
			id: 2,
			route: '/store/accounts/[id]',
			text: 'Login'
		},
		{
			id: 3,
			route: '/store/accounts/[id]/products',
			text: 'Choose product'
		},
		{
			id: 4,
			route: '/store/accounts/[id]/checkout',
			text: 'Buy product'
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
