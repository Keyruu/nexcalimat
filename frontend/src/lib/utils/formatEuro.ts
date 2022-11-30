function formatEuro(node: HTMLElement, balance: number) {
	node.innerText = new Intl.NumberFormat('de-DE', {
		style: 'currency',
		currency: 'EUR',
		minimumFractionDigits: 2,
		maximumFractionDigits: 2
	}).format(balance);
}

export function toEuro(node: HTMLElement, balance: number) {
	formatEuro(node, balance);
	return {
		update(amountInCent: number) {
			formatEuro(node, amountInCent);
		},
		destroy() {
			//
		}
	};
}
