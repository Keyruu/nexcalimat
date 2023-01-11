import type { Handle } from '@sveltejs/kit';
import { locale } from 'svelte-i18n';
import { sequence } from '@sveltejs/kit/hooks';
import { SvelteKitAuth } from "@auth/sveltekit";
import AzureADProvider from "@auth/core/providers/azure-ad";

const i18n: Handle = async ({ event, resolve }) => {
	const lang = event.request.headers.get('accept-language')?.split(',')[0];
	if (lang) {
		locale.set(lang);
	}

	return resolve(event);
};

export const handle: Handle = sequence(
	SvelteKitAuth({
		providers: [AzureADProvider({
			clientId: process.env.AZURE_AD_CLIENT_ID,
			clientSecret: process.env.AZURE_AD_CLIENT_SECRET,
			tenantId: process.env.AZURE_AD_TENANT_ID,
		}),]
	}),
	i18n
);