import { UserManager } from "oidc-client-ts";


export const userManager = new UserManager({
  authority: "https://login.microsoftonline.com/6d5abf7c-b95b-423e-ab06-2781961910e5/v2.0",
  client_id: "6ab7a20c-f6b2-497b-a2f2-2e0f3e356610",
  redirect_uri: "http://localhost:5173/admin/login/callback",
  response_type: "code",
  scope: "openid profile email offline_access",
  post_logout_redirect_uri: "http://localhost:5173/",
  silent_redirect_uri: "http://localhost:5173/admin/login/callback",
  automaticSilentRenew: true,
});

userManager.events.addAccessTokenExpired(async () => {
  const user = await userManager.getUser();
  console.log("addAccessTokenExpiring", user);
});

userManager.events.addSilentRenewError(async () => {
  const user = await userManager.getUser();
  console.log("addSilentRenewError", user);
});

userManager.events.addUserLoaded(async (user) => {
  user.expires_at = user.profile.exp;
  userManager.storeUser(user);
  console.log("addUserLoaded", user);
})
