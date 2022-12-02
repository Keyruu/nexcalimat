import adapter from '@sveltejs/adapter-static';
import 'dotenv/config';
import preprocess from 'svelte-preprocess';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	// Consult https://github.com/sveltejs/svelte-preprocess
	// for more information about preprocessors
	preprocess: [
		preprocess({
			postcss: true
		})
	],

	kit: {
		adapter: adapter({
			precompress: false,
			strict: true,
			fallback: 'index.html',
			prerender: {
				entries: []
			}
		}),
		paths: {
			base: `${process.env.VITE_BASEPATH}`
		}
	}
};

export default config;
