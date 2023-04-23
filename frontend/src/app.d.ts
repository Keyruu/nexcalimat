// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces

import type { User } from "@auth/core/types";

// and what to do when importing types
declare global {
	declare namespace App {
		// interface Locals {}
		interface PageData {
			session: {
				user?: User
			}
		}

		interface Session {
			header: string
		}

		interface Metadata {
			header: string
		}

		// interface Error {}
		// interface Platform {}
	}
}
