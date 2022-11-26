import type { Account } from '$lib/graphql/generated/graphql';

export function getImageUrl(
	account:
		| Account
		| {
				__typename?: 'Account' | undefined;
				id: any;
				name: string | null;
				email: string | null;
				balance: any;
				picture: string | null;
				extId: string | null;
		  }
) {
	return account.picture && account.picture.length
		? `${import.meta.env.VITE_BACKEND_URL}/api/v1/image/${account.picture}`
		: `https://ui-avatars.com/api/?name=${encodeURIComponent(account.name)}`;
}
