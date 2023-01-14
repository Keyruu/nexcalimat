<script lang="ts">
	import Footer from '$lib/components/main/Footer.svelte';
	import Header from '$lib/components/main/Header.svelte';
	import { ApolloClient, InMemoryCache } from '@apollo/client/core';
	import { HttpLink } from '@apollo/client/link/http';
	import { setClient } from 'svelte-apollo';
	import '../app.scss';
	import { PUBLIC_BACKEND_URL } from '$env/static/public'

	function createApolloClient() {
		const link = new HttpLink({
			uri: `${PUBLIC_BACKEND_URL}/graphql`
		});
		const cache = new InMemoryCache();
		const client = new ApolloClient({
			link,
			cache
		});
		return client;
	}

	const client = createApolloClient();
	setClient(client);
</script>

<svelte:head>
	<title>nexcalimat</title>
</svelte:head>

<div class="flex h-screen flex-col justify-center overflow-hidden">
	<Header />
	<div class="flex flex-1 flex-col overflow-y-auto background">
		<main class="justify-center items-center flex flex-col grow max-h-full">
			<slot />
		</main>
		<Footer />
	</div>
</div>

<style lang="scss">
	.background {
		background-image: url("/img/blurry-gradient-haikei.svg");
		background-repeat: no-repeat;
		background-position: center;
		background-size: cover;
	}
</style>
