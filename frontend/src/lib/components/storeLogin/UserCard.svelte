<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import type { Account } from '$lib/generated/graphql';
	import { getImageUrl } from '$lib/utils/accountUtils.js';
	import { toEuro } from '$lib/utils/formatEuro';
	import { Avatar } from '@skeletonlabs/skeleton';

	export let account: Account;

	function navigateToLogin() {
		goto(`${base}/store/accounts/${account.id}`);
	}

	$: img = getImageUrl(account as Account);
</script>

<div
	class="card card-hover variant-glass-surface w-48 cursor-pointer shadow-md hover:variant-ghost-surface hover:shadow-xl"
	on:mouseup="{navigateToLogin}"
>
	<figure class="flex justify-center pb-2 pt-5">
		<Avatar
			initials="{account.name
				?.split(' ')
				.map((n) => n[0])
				.join('')}"
			background="variant-filled-surface"
			class="h-20 w-20"
		/>
	</figure>

	<section class="mb-8 mt-4 flex flex-col items-center justify-center text-center">
		<h3 class="group-hover:text-accent block truncate text-base font-bold">{account.name}</h3>
		<p class="block truncate" use:toEuro="{account.balance}"></p>
	</section>
</div>
