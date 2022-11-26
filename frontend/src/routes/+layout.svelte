<script lang="ts">
	import Footer from '$lib/components/main/Footer.svelte';
	import Header from '$lib/components/main/Header.svelte';
	import { ApolloClient, InMemoryCache } from '@apollo/client/core';
	import { HttpLink } from '@apollo/client/link/http';
	import { setClient } from 'svelte-apollo';
	import '../app.scss';

	function createApolloClient() {
		const link = new HttpLink({
			uri: `${import.meta.env.VITE_BACKEND_URL}/graphql`
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
