import type { Account } from '$lib/types/Account';

export function getImageUrl(account: Account) {
	return account.picture && account.picture.length
		? `${import.meta.env.VITE_BACKEND_URL}/api/v1/image/${account.picture}`
		: `https://ui-avatars.com/api/?name=${encodeURIComponent(account.name)}`;
}
