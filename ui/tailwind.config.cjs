/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ['./src/**/*.{html,js,svelte,ts}'],
	theme: {
		extend: {}
	},
	plugins: [require('@tailwindcss/typography'), require('daisyui')],
	daisyui: {
		themes: [
			'halloween',
			{
				dark: {
					primary: '#4689ff',
					secondary: '#3730a3',
					accent: '#8a88fc',
					neutral: '#1D1929',
					'base-100': '#212121',
					info: '#A2C8F1',
					success: '#6EEDBA',
					warning: '#E7A01D',
					error: '#EC4B7E'
				}
			}
		]
	}
};
