<script lang="ts">
	import { page } from '$app/stores';
	import { pinHeader } from '$lib/stores/authHeader';
	import { onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { URL } from 'url';

	const steps = [
		{
			id: 1,
			route: '/store/accounts',
			text: $_('steps.select-account')
		},
		{
			id: 2,
			route: '/store/accounts/[accountId]',
			text: $_('steps.login')
		},
		{
			id: 3,
			route: '/store/accounts/[accountId]/products',
			text: $_('steps.choose-product')
		},
		{
			id: 4,
			route: '/store/accounts/[accountId]/products/[productId]',
			text: $_('steps.buy-product')
		}
	];

	function isCurrentStep(index: number, currentRoute: string | null): boolean {
		const step = steps.find((s) => s.route === currentRoute);
		return !!step && index <= step.id;
	}

	function getStepHref(index: number, currentRoute: string | null, currentUrl: URL): string {
		const indexOfCurrentRoute = steps.findIndex((step) => step.route === currentRoute);
		if (index < indexOfCurrentRoute) {
			const urlSplit = currentUrl.pathname.split('/');
			for (let i = 0; i < indexOfCurrentRoute - index; i++) {
				urlSplit.pop();
			}
			return urlSplit.join('/');
		}

		return '';
	}

	onDestroy(() => {
		pinHeader.set(null);
	});
</script>

<div class="flex flex-col items-center p-4 overflow-x-hidden">
	{#if ['/history', '/store/leaderboard'].some((r) => $page.route.id?.includes(r)) === false}
		<div
			class="md:mb-16 md:mt-6 flex w-[80%] items-center justify-center invisible h-0 md:h-auto md:visible lg:visible"
		>
			<ol
				class="flex w-full items-center text-center text-sm font-medium text-gray-500 dark:text-gray-400 sm:text-base"
			>
				{#each steps as step, i (step.id)}
					<li
						class="{i !== steps.length - 1
							? 'after:border-1 after:mx-6 after:hidden after:h-1 after:w-full after:border-b after:border-gray-200 dark:after:border-gray-700 sm:after:inline-block sm:after:content-[\'\'] md:w-full xl:after:mx-10'
							: ''}
					flex items-center {isCurrentStep(step.id, $page.route.id) ? 'text-blue-500 dark:text-blue-500' : ''}"
					>
						<a
							href="{getStepHref(i, $page.route.id, $page.url)}"
							class="unstyled inline-flexs variant-glass-surface items-center rounded-lg text-lg
						{step.route === $page.route.id ? 'font-extrabold' : ''}
						{i !== steps.length - 1 ? 'after:mx-2 after:text-gray-200 after:content-[\'/\'] dark:after:text-gray-500' : ''}
						{i === 0 || (i !== steps.length - 1 && $page.route.id === steps[i + 1].route) ? 'cursor-pointer' : ''}
						sm:after:hidden"
						>
							{step.text}
						</a>
					</li>
				{/each}
			</ol>
		</div>
	{/if}

	<slot />
</div>

<style lang="scss">
</style>
