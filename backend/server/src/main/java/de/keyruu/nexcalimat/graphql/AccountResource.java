package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.PinInput;
import de.keyruu.nexcalimat.repository.AccountRepository;

@GraphQLApi
public class AccountResource {
  @Inject
  AccountRepository _accountRepository;

  @Inject
  JsonWebToken _jwt;

  @ConfigProperty(name = "de.keyruu.nexcalimat.userGroup")
  String _userGroup;

  @Query
  @Description("Say hello")
  public List<Account> accounts() {
    return _accountRepository.listAll();
  }

  @Query
  @Description("Sign Up")
  @RolesAllowed("b7f48c80-ba64-4933-a23a-0029b272631e")
  public Account signUp(PinInput pinInput) {
    return _accountRepository.signUp(pinInput, _jwt);
  }

  @Mutation
  @Description("")
  public Account account(Account accountInput) {
    _accountRepository.persist(accountInput);
    return accountInput;
  }
}
