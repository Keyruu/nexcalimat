<script lang="ts">
	import type { Account } from '$lib/generated/graphql';
	import { getImageUrl } from '$lib/utils/accountUtils.js';
	import { centToEuro } from '$lib/utils/formatEuro';
	import { Avatar } from '@skeletonlabs/skeleton';

	export let account: Account;

	$: img = getImageUrl(account as Account);
</script>

<div
	class="card card-hover variant-glass-surface w-48 cursor-pointer shadow-md hover:variant-ghost-surface hover:shadow-xl"
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
		<div class="flex h-16 w-[90%] items-center justify-center">
			<h3 class="group-hover:text-accent line-clamp-2 text-center align-middle text-base font-bold">
				{account.name}
			</h3>
		</div>
		<p class="truncate" use:centToEuro>{account.balance}</p>
	</section>
</div>
