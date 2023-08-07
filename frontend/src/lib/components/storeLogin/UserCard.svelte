<script lang="ts">
	import type { Account } from '$lib/generated/graphql';
	import { getInitials } from '$lib/utils/accountUtils.js';
	import { numberCentToEuro } from '$lib/utils/formatEuro';
	import { getAccountPicture } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { Avatar, modalStore, type ModalComponent, type ModalSettings } from '@skeletonlabs/skeleton';
	import { createEventDispatcher } from 'svelte';
	import BalanceModal from '../admin/BalanceModal.svelte';

	const dispatch = createEventDispatcher();

	export let account: Account;
	export let adminMode = false;

	let editMode = false;

	function triggerBalanceModal(account: Account | null) {
		const modalComponent: ModalComponent = {
			// Pass a reference to your custom component
			ref: BalanceModal,
			props: {
				account: account
			}
		};

		const modal: ModalSettings = {
			type: 'component',
			response: (r: boolean) => console.log('response:', r),
			component: modalComponent
		};
		modalStore.trigger(modal);
	}
</script>

<div
	class="card card-hover variant-glass-surface w-48 cursor-pointer shadow-md hover:variant-ghost-surface hover:shadow-xl"
	on:mouseleave="{() => (editMode = false)}"
	role="figure"
>
	{#if adminMode}
		<div class="absolute top-0 right-0">
			<button class="btn-icon hover:variant-soft" on:click="{() => (editMode = !editMode)}"
				><Icon icon="fa6-solid:ellipsis-vertical" /></button
			>
			{#if editMode}
				<div class="btn-group-vertical variant-filled-surface absolute z-50">
					<button on:click="{() => triggerBalanceModal(account)}" class="variant-filled-success"
						><Icon icon="fa6-solid:sack-dollar" /></button
					>
					<button class="variant-filled-error"><Icon icon="fa-solid:archive" /></button>
				</div>
			{/if}
		</div>
	{/if}
	<figure class="flex justify-center pb-2 pt-5">
		<Avatar
			src="{getAccountPicture(account)}"
			initials="{getInitials(account)}"
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
		<p class="truncate">{numberCentToEuro(account.balance)}</p>
	</section>
</div>
