<script lang="ts">
	import {
		UpdateBalanceDocument,
		type Account,
		type UpdateBalanceMutation,
		type UpdateBalanceMutationVariables
	} from '$lib/generated/graphql';
	import Icon from '@iconify/svelte';
	import { _ } from 'svelte-i18n';
	// Stores
	import { SlideToggle, modalStore } from '@skeletonlabs/skeleton';
	import { mutationStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import type { Unsubscriber } from 'svelte/store';
	import { client } from '../../../urqlClient';

	/** Exposes parent props to this component. */
	export let parent: any;

	export let account: Account;
	let addMode = true;
	let amount = 0;

	const updateBalance = (id: number, balance: number) =>
		mutationStore<UpdateBalanceMutation, UpdateBalanceMutationVariables>({
			client,
			query: UpdateBalanceDocument,
			variables: {
				id,
				balance
			}
		});

	let unsubscribe: Unsubscriber;

	// We've created a custom submit function to pass the response and close the modal.
	async function onFormSubmit() {
		if (account.id && account.balance != null) {
			let balance;
			if (addMode) {
				balance = account.balance + amount * 100;
			} else {
				balance = amount * 100;
			}

			unsubscribe = updateBalance(account.id, balance).subscribe(({ data, error }) => {
				console.log(data);
				console.log(error);
				if (data?.updateAccount?.balance != null) {
					if ($modalStore[0].response) $modalStore[0].response(true);
					modalStore.close();
				}
			});
		}
	}

	function toggleMode(): void {
		if (addMode && account.balance) {
			amount = account.balance / 100;
		} else {
			amount = 0;
		}
	}

	onDestroy(() => {
		if (unsubscribe) {
			unsubscribe();
		}
	});

	// Base Classes
	const cBase = 'card p-4 w-modal shadow-xl space-y-4';
</script>

<!-- @component This example creates a simple form modal. -->

{#if $modalStore[0]}
	<div class="{cBase}">
		<h3 class="modal-header font-bold">{`${$_('admin.balance-modal.header')} ${account.name}`}</h3>
		<SlideToggle name="slider-label" on:click="{toggleMode}" bind:checked="{addMode}">
			{#if addMode}
				<p>{$_('admin.balance-modal.add-mode')}</p>
			{:else}
				<p>{$_('admin.balance-modal.set-mode')}</p>
			{/if}
		</SlideToggle>
		<hr />
		<form class="flex justify-center items-center" on:submit="{onFormSubmit}">
			<label class="label">
				<span>Balance</span>
				<div class="input-group input-group-divider grid-cols-[auto_1fr_auto] w-64">
					<input type="number" placeholder="Add..." step=".01" min="0" bind:value="{amount}" />
					<div class="input-group-shim"><Icon icon="fa6-solid:euro-sign" /></div>
				</div>
			</label>
		</form>
		<hr />
		<!-- prettier-ignore -->
		<footer class="modal-footer {parent.regionFooter}">
        <button class="btn variant-filled-surface" on:click={() => modalStore.close()}>{$_('cancel')}</button>
        <button class="btn variant-filled-success" on:click={onFormSubmit}>{addMode ? $_('admin.balance-modal.add') : $_('admin.balance-modal.set')}</button>
    </footer>
	</div>
{/if}
