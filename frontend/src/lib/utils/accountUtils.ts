import type { Account } from '$lib/generated/graphql';

export function getInitials(account: Account) {
	if (!account.name) return ''
	return account.name
		?.split(' ')
		.map((n) => n[0])
		.join('')
}
