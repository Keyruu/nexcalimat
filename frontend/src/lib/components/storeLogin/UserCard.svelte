<script lang="ts">
	import {
		DeleteAccountDocument,
		EraseAccountDocument,
		ReactivateAccountDocument,
		type Account,
		type DeleteAccountMutation,
		type DeleteAccountMutationVariables,
		type EraseAccountMutation,
		type EraseAccountMutationVariables,
		type ReactivateAccountMutation,
		type ReactivateAccountMutationVariables
	} from '$lib/generated/graphql';
	import { getInitials } from '$lib/utils/accountUtils.js';
	import { numberCentToEuro } from '$lib/utils/formatEuro';
	import { getAccountPicture } from '$lib/utils/pictureUtils';
	import { handleError } from '$lib/utils/storeError';
	import { toastSuccess } from '$lib/utils/toastUtils';
	import Icon from '@iconify/svelte';
	import { Avatar, modalStore, toastStore, type ModalComponent, type ModalSettings } from '@skeletonlabs/skeleton';
	import { mutationStore } from '@urql/svelte';
	import { createEventDispatcher, onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';
	import { client } from '../../../urqlClient';
	import BalanceModal from '../admin/BalanceModal.svelte';

	const dispatch = createEventDispatcher();

	export let account: Account;
	export let adminMode = false;
	export let deletionMode = false;

	let editMode = false;

	const deleteAccount = () =>
		mutationStore<DeleteAccountMutation, DeleteAccountMutationVariables>({
			client,
			query: DeleteAccountDocument,
			variables: {
				id: account.id!
			}
		});

	const reactiveAccount = () =>
		mutationStore<ReactivateAccountMutation, ReactivateAccountMutationVariables>({
			client,
			query: ReactivateAccountDocument,
			variables: {
				id: account.id!
			}
		});

	const eraseAccount = () =>
		mutationStore<EraseAccountMutation, EraseAccountMutationVariables>({
			client,
			query: EraseAccountDocument,
			variables: {
				id: account.id!
			}
		});

	let deleteAccountUnsubscribe: Unsubscriber,
		reactiveAccountUnsubscribe: Unsubscriber,
		eraseAccountUnsubscribe: Unsubscriber;

	function triggerBalanceModal() {
		const modalComponent: ModalComponent = {
			// Pass a reference to your custom component
			ref: BalanceModal,
			props: {
				account
			}
		};

		const modal: ModalSettings = {
			type: 'component',
			response: (r: boolean) => {
				if (r) {
					toastStore.trigger(toastSuccess($_('toast.balance.success')));
					dispatch('refetch');
				}
			},
			component: modalComponent
		};
		modalStore.trigger(modal);
	}

	function triggerDeletionModal() {
		const modal: ModalSettings = {
			type: 'confirm',
			// Data
			title: $_('admin.delete-account-modal.title'),
			body: $_('admin.delete-account-modal.body'),
			buttonTextCancel: $_('cancel'),
			buttonTextConfirm: $_('confirm'),
			// TRUE if confirm pressed, FALSE if cancel pressed
			response: (r: boolean) => {
				if (r) {
					deleteAccountUnsubscribe = deleteAccount().subscribe(({ data, error }) => {
						if (data?.deleteAccount) {
							console.log('Account deleted');
							toastStore.trigger(toastSuccess($_('toast.delete-account.success')));
							dispatch('refetch');
						} else if (error) {
							handleError(error);
						}
					});
				}
			}
		};
		modalStore.trigger(modal);
	}
	function triggerReactivationModal() {
		const modal: ModalSettings = {
			type: 'confirm',
			// Data
			title: $_('admin.reactivate-account-modal.title'),
			body: $_('admin.reactivate-account-modal.body'),
			buttonTextCancel: $_('cancel'),
			buttonTextConfirm: $_('confirm'),
			// TRUE if confirm pressed, FALSE if cancel pressed
			response: (r: boolean) => {
				if (r) {
					reactiveAccountUnsubscribe = reactiveAccount().subscribe(({ data, error }) => {
						if (data?.reactivateAccount) {
							console.log('Account reactivated');
							toastStore.trigger(toastSuccess($_('toast.reactivate-account.success')));
							dispatch('refetch');
						} else if (error) {
							handleError(error);
						}
					});
				}
			}
		};
		modalStore.trigger(modal);
	}
	function triggerErasingModal() {
		const modal: ModalSettings = {
			type: 'confirm',
			// Data
			title: $_('admin.erase-account-modal.title'),
			body: $_('admin.erase-account-modal.body'),
			buttonTextCancel: $_('cancel'),
			buttonTextConfirm: $_('confirm'),
			// TRUE if confirm pressed, FALSE if cancel pressed
			response: (r: boolean) => {
				if (r) {
					eraseAccountUnsubscribe = eraseAccount().subscribe(({ data, error }) => {
						if (data?.eraseAccount) {
							console.log('Account erased');
							toastStore.trigger(toastSuccess($_('toast.erase-account.success')));
							dispatch('refetch');
						} else if (error) {
							handleError(error);
						}
					});
				}
			}
		};
		modalStore.trigger(modal);
	}

	onDestroy(() => {
		deleteAccountUnsubscribe?.();
		reactiveAccountUnsubscribe?.();
		eraseAccountUnsubscribe?.();
	});
</script>

<div
	class="card card-hover variant-glass-surface w-48 cursor-pointer shadow-md hover:variant-ghost-surface hover:shadow-xl"
	on:mouseleave="{() => (editMode = false)}"
	role="figure"
>
	{#if adminMode || deletionMode}
		<div class="absolute top-0 right-0">
			<button class="btn-icon hover:variant-soft" on:click="{() => (editMode = !editMode)}"
				><Icon icon="fa6-solid:ellipsis-vertical" /></button
			>
			{#if editMode && adminMode}
				<div class="btn-group-vertical variant-filled-surface absolute z-50">
					<button on:click="{triggerBalanceModal}" class="variant-filled-success"
						><Icon icon="fa6-solid:sack-dollar" /></button
					>
					<button on:click="{triggerDeletionModal}" class="variant-filled-error"
						><Icon icon="fa-solid:archive" /></button
					>
				</div>
			{:else if editMode && deletionMode}
				<div class="btn-group-vertical variant-filled-surface absolute z-50">
					<button on:click="{triggerReactivationModal}" class="variant-filled-success"
						><Icon icon="fa6-solid:arrow-left" /></button
					>
					<button on:click="{triggerErasingModal}" class="variant-filled-error"
						><Icon icon="fa6-solid:trash-can" /></button
					>
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
