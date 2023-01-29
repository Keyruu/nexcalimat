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
		setTimeout(() => reset(), 400);
	}

	export function triggerMiss() {
		console.debug('trigger miss');
		error = true;
		setTimeout(() => reset(), 500);
	}

	$: buttonsDisabled = success || error;
	$: {
		if (value.length === 4) {
			setTimeout(() => {
				submit();
			}, 100);
		}
	}
</script>

<div class:pincode--success="{success}" class:pincode--miss="{error}" class="{`${$$props.class} pincode`}">
	<div
		class:pincode__fields--miss="{error}"
		class="pincode__fields mx-auto mt-5 mb-6 flex max-w-[250px] items-center justify-between px-5"
	>
		<span class:active="{value.length >= 1}"></span>
		<span class:active="{value.length >= 2}"></span>
		<span class:active="{value.length >= 3}"></span>
		<span class:active="{value.length >= 4}"></span>
	</div>
	<div class="mx-auto grid w-fit grid-cols-3 gap-y-2 gap-x-4">
		{#each Array(9) as _, i}
			<div class="flex items-center justify-center">
				<button
					disabled="{buttonsDisabled}"
					class="btn-outline btn-info btn h-20 w-20 rounded-full text-xl"
					on:click="{() => select(i + 1)}">{i + 1}</button
				>
			</div>
		{/each}
		<div class="flex items-center justify-center">
			<button
				class="btn-outline btn-error btn h-20 w-20 rounded-full text-xl"
				disabled="{!value || buttonsDisabled}"
				on:click="{deleteLast}">←</button
			>
		</div>
		<div class="flex items-center justify-center">
			<button
				disabled="{buttonsDisabled}"
				class="btn-outline btn-info btn h-20 w-20 rounded-full text-xl"
				on:click="{() => select(0)}">0</button
			>
		</div>
	</div>
</div>

<style lang="scss">
	.pincode {
		&__fields {
			&--miss {
				animation: miss 0.7s ease-out 1;
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
