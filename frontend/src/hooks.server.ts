import AzureADProvider from '@auth/core/providers/azure-ad';
import { SvelteKitAuth } from '@auth/sveltekit';
import type { Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { locale } from 'svelte-i18n';

const i18n: Handle = async ({ event, resolve }) => {
	const lang = event.request.headers.get('accept-language')?.split(',')[0];
	if (lang) {
		locale.set(lang);
	}

	return resolve(event);
};

const auth: Handle = SvelteKitAuth({
	providers: [
		AzureADProvider({
			clientId: import.meta.env.VITE_AZURE_AD_CLIENT_ID,
			clientSecret: import.meta.env.VITE_AZURE_AD_CLIENT_SECRET,
			tenantId: import.meta.env.VITE_AZURE_AD_TENANT_ID
		})
	],
	secret: import.meta.env.VITE_AZURE_AD_CLIENT_SECRET
});

export const handle: Handle = sequence(auth, i18n);
