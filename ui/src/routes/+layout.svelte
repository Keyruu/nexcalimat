<script lang="ts">
	import Footer from '$lib/components/main/Footer.svelte';
	import Header from '$lib/components/main/Header.svelte';
	import '../app.scss';
</script>

<svelte:head>
	<title>Excalimat</title>
</svelte:head>

<div class="flex h-screen flex-col overflow-hidden">
	<Header />
	<div class="flex flex-1 flex-col overflow-y-auto">
		<main class="container mx-auto flex grow flex-col">
			<slot />
		</main>
		<Footer />
	</div>
</div>
