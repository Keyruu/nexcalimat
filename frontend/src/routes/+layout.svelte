<script lang="ts">
	import { PUBLIC_BACKEND_URL } from '$env/static/public';
	import Footer from '$lib/components/main/Footer.svelte';
	import Header from '$lib/components/main/Header.svelte';
	import '../app.scss';
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
