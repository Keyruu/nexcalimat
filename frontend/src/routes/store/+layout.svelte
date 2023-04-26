<script lang="ts">
	import { goto } from '$app/navigation';
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

	function isCurrentStep(index: number, currentRoute: string | null): boolean {
		const step = steps.find((s) => s.route === currentRoute);
		return !!step && index <= step.id;
	}

	onDestroy(() => {
		accountToken.set(undefined);
	});
</script>

<div class="flex flex-col items-center p-4">
	<div class="mb-16 mt-6 flex w-[80%] items-center justify-center">
		<ol class="flex w-full items-center text-center text-sm font-medium text-gray-500 dark:text-gray-400 sm:text-base">
			{#each steps as step, i (step.id)}
				<li
					class="{i !== steps.length - 1
						? "after:border-1 after:mx-6 after:hidden after:h-1 after:w-full after:border-b after:border-gray-200 dark:after:border-gray-700 sm:after:inline-block sm:after:content-[''] md:w-full xl:after:mx-10"
						: ''}
					flex items-center {isCurrentStep(step.id, $page.route.id) ? 'text-blue-600 dark:text-blue-500' : ''}"
				>
					<span
						class="inline-flex items-center text-lg
						{step.route === $page.route.id ? 'font-extrabold' : ''}
						{i !== steps.length - 1 ? "after:mx-2 after:text-gray-200 after:content-['/'] dark:after:text-gray-500" : ''} 
						{i === 0 ? 'cursor-pointer' : ''}
						sm:after:hidden"
						on:click="{() => {
							if (step.id === 1) goto(step.route);
						}}"
					>
						{step.text}
					</span>
				</li>
			{/each}
		</ol>
	</div>

	<slot />
</div>

<style lang="scss">
</style>
