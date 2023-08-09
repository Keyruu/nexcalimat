<script lang="ts">
	import { setContextClient } from '@urql/svelte';
	// Your selected Skeleton theme:
	import '@skeletonlabs/skeleton/themes/theme-skeleton.css';

	// This contains the bulk of Skeletons required styles:
	import '@skeletonlabs/skeleton/styles/all.css';

	import '../app.scss';

	import Header from '$lib/components/main/Header.svelte';
	import { Modal, Toast } from '@skeletonlabs/skeleton';
	import { client } from '../urqlClient';

	import dayjs from 'dayjs';
	import timezone from 'dayjs/plugin/timezone';
	import utc from 'dayjs/plugin/utc';

	dayjs.extend(timezone);
	dayjs.extend(utc);

	setContextClient(client);
</script>

<svelte:head>
	<title>nexcalimat</title>
</svelte:head>

<Modal />
<div class="flex h-screen flex-col justify-center overflow-hidden">
	<Header />
	<div class="background flex flex-1 flex-col overflow-y-auto">
		<main class="flex grow flex-col">
			<slot />
		</main>
		<!-- <Footer /> -->
	</div>
	<Toast />
</div>

<style lang="scss">
	.background {
		// background-image: url('/img/blurry-gradient-haikei.svg');
		// background-repeat: no-repeat;
		// background-position: center;
		// background-size: cover;
		background-color: hsla(0, 0%, 10%, 1);
		background-image: radial-gradient(at 66% 86%, hsla(240, 100%, 20%, 0.74) 0px, transparent 50%),
			radial-gradient(at 77% 13%, hsla(173, 0%, 8%, 0.41) 0px, transparent 50%),
			radial-gradient(at 57% 66%, hsla(221, 0%, 11%, 1) 0px, transparent 50%),
			radial-gradient(at 0% 100%, hsla(225, 98%, 35%, 1) 0px, transparent 50%),
			radial-gradient(at 38% 9%, hsla(237, 96%, 29%, 1) 0px, transparent 50%),
			radial-gradient(at 6% 9%, hsla(0, 0%, 10%, 1) 0px, transparent 50%),
			radial-gradient(at 92% 40%, hsla(239, 100%, 40%, 1) 0px, transparent 50%),
			radial-gradient(at 30% 46%, hsla(12, 0%, 0%, 1) 0px, transparent 50%);
	}
</style>
