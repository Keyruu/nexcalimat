import { goto } from "$app/navigation";
import { MyAccountDocument, type Account, type MyAccountQuery } from "$lib/generated/graphql";
import { queryStore } from "@urql/svelte";
import { User, UserManager } from "oidc-client-ts";
import { writable } from "svelte/store";
import { client } from "../../urqlClient";
import { authHeader } from "./authHeader";
import { PUBLIC_BASE_URL, PUBLIC_OIDC_AUTHORITY, PUBLIC_OIDC_CLIENT_ID, PUBLIC_OIDC_SCOPE } from "$env/static/public";


export const userManager = new UserManager({
  authority: PUBLIC_OIDC_AUTHORITY,
  client_id: PUBLIC_OIDC_CLIENT_ID,
  redirect_uri: `${PUBLIC_BASE_URL}/admin/login/callback`,
  response_type: "code",
  scope: PUBLIC_OIDC_SCOPE,
  post_logout_redirect_uri: PUBLIC_BASE_URL,
  silent_redirect_uri: `${PUBLIC_BASE_URL}/admin/login/callback`,
  automaticSilentRenew: true,
});

export const oidcUser = writable<User | null>(null);
export const account = writable<Account | undefined>(undefined);

// userManager.events.addAccessTokenExpired(async () => {
//   const user = await userManager.getUser();
//   console.log("addAccessTokenExpired", user);
// });

userManager.events.addSilentRenewError(async (error) => {
  const user = await userManager.getUser();
  console.error("addSilentRenewError", user, error);
});

userManager.events.addUserLoaded(async (user) => {
  user.expires_at = user.profile.exp;
  userManager.storeUser(user);
  oidcUser.set(user);

  localStorage.setItem('authHeader', `Bearer ${user.id_token}`);
  authHeader.set(`Bearer ${user.id_token}`);



  console.log("addUserLoaded", user);
})

export function handleMyAccount() {
  const accountQuery = queryStore<MyAccountQuery>({
    client,
    query: MyAccountDocument,
  })

  accountQuery.subscribe((result) => {
    if (result.data?.myAccount) {
      const myAccount = result.data.myAccount;
      account.set(myAccount);
      if (!myAccount.hasPin) {
        goto('/admin/set-pin')
      }
    } else if (result.error) {
      if (result.error.graphQLErrors.length > 0) {
        if (result.error.graphQLErrors[0].extensions?.code === 'account-not-found') {
          goto('/admin/set-pin')
        }
      }
      console.log(result.error.graphQLErrors)
    }
  })
}