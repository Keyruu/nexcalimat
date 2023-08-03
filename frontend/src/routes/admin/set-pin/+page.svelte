<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Keypad from '$lib/components/storeLogin/Keypad.svelte';
	import {
		SetPinDocument,
		SignUpDocument,
		type SetPinMutation,
		type SetPinMutationVariables,
		type SignUpMutation,
		type SignUpMutationVariables
	} from '$lib/generated/graphql';
	import { mutationStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import { get, type Unsubscriber } from 'svelte/store';
	import { client } from '../../../urqlClient';

	let triggerSuccess: () => void;
	let triggerMiss: () => void;
	let pin: string = '';

	const setPin = () =>
		mutationStore<SetPinMutation, SetPinMutationVariables>({
			client,
			query: SetPinDocument,
			variables: {
				pin
			}
		});

	const signUp = () =>
		mutationStore<SignUpMutation, SignUpMutationVariables>({
			client,
			query: SignUpDocument,
			variables: {
				pin
			}
		});

	let unsubscribe: Unsubscriber;

	function handleSubmit() {
		console.log('submit was pressed', pin);
		if (get(page).url.searchParams.get('signup') === 'true') {
			unsubscribe = signUp().subscribe((result) => {
				if (result.data?.signUp?.id) {
					console.log(result);
					triggerSuccess();
					goto('/admin');
				} else if (result.error) {
					triggerMiss();
				}
			});
		} else {
			unsubscribe = setPin().subscribe((result) => {
				if (result.data?.pin) {
					console.log(result);
					triggerSuccess();
					goto('/admin');
				} else if (result.error) {
					triggerMiss();
				}
			});
		}
	}

	onDestroy(() => {
		if (unsubscribe) unsubscribe();
	});
</script>

<div class="mt-4 flex flex-col items-center justify-center">
	<h1 class="my-8">Set your PIN</h1>
	<div class="card variant-ghost-surface rounded-xl p-10">
		<Keypad
			bind:value="{pin}"
			on:submit="{handleSubmit}"
			bind:triggerSuccess="{triggerSuccess}"
			bind:triggerMiss="{triggerMiss}"
		/>
	</div>
</div>
