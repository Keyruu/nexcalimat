import { goto } from "$app/navigation";
import { env } from "$env/dynamic/public";
import { MyAccountDocument, type Account, type MyAccountQuery } from "$lib/generated/graphql";
import { queryStore } from "@urql/svelte";
import { User, UserManager } from "oidc-client-ts";
import { writable } from "svelte/store";
import { client } from "../../urqlClient";
import { authHeader } from "./authHeader";


export const userManager = new UserManager({
  authority: env.PUBLIC_OIDC_AUTHORITY,
  client_id: env.PUBLIC_OIDC_CLIENT_ID,
  redirect_uri: `${env.PUBLIC_BASE_URL}/admin/login/callback`,
  response_type: "code",
  scope: env.PUBLIC_OIDC_SCOPE,
  post_logout_redirect_uri: env.PUBLIC_BASE_URL,
  silent_redirect_uri: `${env.PUBLIC_BASE_URL}/admin/login/callback`,
  automaticSilentRenew: true,
});

export const oidcUser = writable<User | null>(null);
export const account = writable<Account | undefined>(undefined);

// userManager.events.addAccessTokenExpired(async () => {
//   const user = await userManager.getUser();
//   console.log("addAccessTokenExpired", user);
// });

userManager.events.addAccessTokenExpired(async () => {
  console.error("addAccessTokenExpired");
  userManager.signinRedirect();
});

userManager.events.addSilentRenewError(async (error) => {
  const user = await userManager.getUser();
  console.error("addSilentRenewError", user, error);
  userManager.signinRedirect();
});

userManager.events.addUserLoaded(async (user) => {
  user.expires_at = user.profile.exp;
  userManager.storeUser(user);

  setUser(user);

  console.log("addUserLoaded", user);
})

export function setUser(user: User) {
  oidcUser.set(user);

  localStorage.setItem('authHeader', `Bearer ${user.id_token}`);
  authHeader.set(`Bearer ${user.id_token}`);
}

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