<script lang="ts">
	import Footer from '$lib/components/main/Footer.svelte';
	import Header from '$lib/components/main/Header.svelte';
	import { setContextClient } from '@urql/svelte';
	import '../app.scss';

	import { client } from '../urqlClient';

	localStorage.setItem('authHeader', '');
	setContextClient(client);
</script>

<svelte:head>
	<title>nexcalimat</title>
</svelte:head>

<div class="flex h-screen flex-col justify-center overflow-hidden">
	<Header />
	<div class="background flex flex-1 flex-col overflow-y-auto">
		<main class="flex max-h-full grow flex-col items-center justify-center">
			<slot />
		</main>
		<Footer />
	</div>
</div>

<style lang="scss">
	.background {
		background-image: url('/img/blurry-gradient-haikei.svg');
		background-repeat: no-repeat;
		background-position: center;
		background-size: cover;
	}
</style>
