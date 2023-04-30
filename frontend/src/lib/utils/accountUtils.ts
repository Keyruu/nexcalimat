import type { Account } from '$lib/generated/graphql';

export function getImageUrl(account: Account) {
	return account.picture && account.picture.length
		? account.picture
		: `https://ui-avatars.com/api/?name=${encodeURIComponent(account.name ?? '')}`;
}
