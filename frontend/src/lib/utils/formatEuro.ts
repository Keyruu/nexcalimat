import type { Maybe } from "$lib/generated/graphql";

function formatEuro(node: HTMLElement, number: number) {
	node.innerText = new Intl.NumberFormat('de-DE', {
		style: 'currency',
		currency: 'EUR',
		minimumFractionDigits: 2,
		maximumFractionDigits: 2
	}).format(number);
}

export function toEuro(node: HTMLElement) {
	formatEuro(node, Number(node.innerText));
}

export function centToEuro(node: HTMLElement) {
	formatEuro(node, Number(node.innerText) / 100);
}

export function numberCentToEuro(cent: Maybe<number> | undefined): string {
	if (cent == undefined) return '';
	return new Intl.NumberFormat('de-DE', {
		style: 'currency',
		currency: 'EUR',
		minimumFractionDigits: 2,
		maximumFractionDigits: 2
	}).format(cent / 100);
}