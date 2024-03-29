import type { Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { locale } from 'svelte-i18n';

const i18n: Handle = async ({ event, resolve }) => {
	const lang = event.request.headers.get('accept-language')?.split(',')[0];
	if (lang) {
		locale.set(lang);
	}

	return await resolve(event);
};

// const auth: Handle = SvelteKitAuth({
// 	providers: [
// 		AzureADProvider({
// 			clientId: AZURE_AD_CLIENT_ID,
// 			clientSecret: AZURE_AD_CLIENT_SECRET,
// 			tenantId: AZURE_AD_TENANT_ID
// 		})
// 	] as Provider[]
// });

// export const handle: Handle = sequence(auth, i18n);
export const handle: Handle = sequence(i18n);
