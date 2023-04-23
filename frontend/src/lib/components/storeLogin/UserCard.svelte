<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import type { Account } from '$lib/generated/graphql';
	import { getImageUrl } from '$lib/utils/accountUtils.js';
	import { toEuro } from '$lib/utils/formatEuro';

	export let account: Account;

	function navigateToLogin() {
		goto(`${base}/store/accounts/${account.id}`);
	}

	$: img = getImageUrl(account as Account);
</script>

<div
	class="card-compact group card h-52 cursor-pointer bg-neutral shadow-md hover:bg-neutral-focus hover:shadow-xl"
	on:mouseup="{navigateToLogin}"
>
	<figure class="pb-2 pt-5">
		<div class="avatar">
			<div class="w-24 rounded-full ring-2 ring-white">
				<img class="group-hover:brightness-90" src="{img}" alt="account" />
			</div>
		</div>
	</figure>

	<div class="card-body inline-block text-center">
		<h2 class="card-title block truncate text-base group-hover:text-accent">{account.name}</h2>
		<p class="block truncate" use:toEuro="{account.balance}"></p>
	</div>
</div>
