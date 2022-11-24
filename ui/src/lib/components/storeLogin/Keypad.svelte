<script lang="ts">
	import { createEventDispatcher } from 'svelte';

	export let value = '';

	let success = false;
	let error = false;

	const dispatch = createEventDispatcher();

	function select(num: number) {
		if (value.length < 4) {
			value += num;
		}
	}

	function deleteLast() {
		value = value.slice(0, -1);
	}

	function submit() {
		dispatch('submit');
	}

	function reset() {
		value = '';
		success = false;
		error = false;
	}

	export function triggerSuccess() {
		console.debug('trigger success');
		success = true;
		setTimeout(() => reset(), 800);
	}

	export function triggerMiss() {
		console.debug('trigger miss');
		error = true;
		setTimeout(() => reset(), 2000);
	}

	$: buttonsDisabled = success || error;
	$: {
		if (value.length === 4) {
			setTimeout(() => {
				submit();
			}, 200);
		}
	}
</script>

<div class="{`${$$props.class} pincode ${success ? 'pincode--success' : ''} ${error ? 'pincode--miss' : ''}`}">
	<div
		class="{`pincode__fields mx-auto mt-5 mb-10 flex max-w-[250px] items-center justify-between px-5 ${
			error ? 'pincode__fields--miss' : ''
		}`}"
	>
		<span class="{value.length >= 1 ? 'active' : ''}"></span>
		<span class="{value.length >= 2 ? 'active' : ''}"></span>
		<span class="{value.length >= 3 ? 'active' : ''}"></span>
		<span class="{value.length >= 4 ? 'active' : ''}"></span>
	</div>
	<div class="mx-auto grid w-fit grid-cols-3 gap-y-3 gap-x-5">
		{#each Array(9) as _, i}
			<button
				disabled="{buttonsDisabled}"
				class="btn-outline btn-info btn h-20 w-20 rounded-full text-xl"
				on:click="{() => select(i + 1)}">{i + 1}</button
			>
		{/each}
		<button
			class="btn-outline btn-error btn h-20 w-20 rounded-full text-xl"
			disabled="{!value || buttonsDisabled}"
			on:click="{deleteLast}">←</button
		>
		<button
			disabled="{buttonsDisabled}"
			class="btn-outline btn-info btn h-20 w-20 rounded-full text-xl"
			on:click="{() => select(0)}">0</button
		>
	</div>
</div>

<style lang="scss">
	.pincode {
		&__fields {
			&--miss {
				animation: miss 0.8s ease-out 1;
			}

			span {
				@apply relative inline-block h-4 w-4 rounded-full text-center;
				box-shadow: inset 0 0 0 2px theme('colors.white');
				transition: box-shadow 0.2s linear;

				&.active {
					box-shadow: inset 0 0 0 8px theme('colors.white');
				}
			}
		}

		&--success {
			.pincode__fields {
				span {
					box-shadow: inset 0 0 0 8px theme('colors.success');
				}
			}
		}

		&--miss {
			.pincode__fields {
				span {
					box-shadow: inset 0 0 0 8px theme('colors.error');
				}
			}
		}
	}

	@keyframes miss {
		0% {
			transform: translate(0, 0);
		}
		10% {
			transform: translate(-25px, 0);
		}
		20% {
			transform: translate(25px, 0);
		}
		30% {
			transform: translate(-20px, 0);
		}
		40% {
			transform: translate(20px, 0);
		}
		50% {
			transform: translate(-10px, 0);
		}
		60% {
			transform: translate(10px, 0);
		}
		70% {
			transform: translate(-5px, 0);
		}
		80% {
			transform: translate(5px, 0);
		}
		100% {
			transform: translate(0, 0);
		}
	}
</style>
