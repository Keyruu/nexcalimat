import type { Provider } from '@auth/core/providers';
import AzureADProvider from '@auth/core/providers/azure-ad';
import type { Profile } from '@auth/core/types';
import { SvelteKitAuth } from '@auth/sveltekit';
import type { Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { locale } from 'svelte-i18n';
import { AZURE_AD_CLIENT_ID, AZURE_AD_CLIENT_SECRET, AZURE_AD_TENANT_ID } from '$env/static/private'

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
			clientId: AZURE_AD_CLIENT_ID,
			clientSecret: AZURE_AD_CLIENT_SECRET,
			tenantId: AZURE_AD_TENANT_ID
		}) as Provider<Profile>
	],
});

export const handle: Handle = sequence(auth, i18n);
