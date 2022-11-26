package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;

@GraphQLApi
public class AccountResource {
  @Inject
  AccountRepository _accountRepository;

  @Inject
  AccountService _accountService;

  @Inject
  JsonWebToken _jwt;

  @ConfigProperty(name = "de.keyruu.nexcalimat.userGroupName")
  String _userGroup;

  @Query
  @Description("Get all Accounts")
  @RolesAllowed({ Roles.CUSTOMER, Roles.ADMIN })
  public List<Account> accounts() {
    return _accountRepository.listAll();
  }

  @Query
  @Description("Get Account by ID")
  @RolesAllowed({ Roles.CUSTOMER, Roles.USER, Roles.ADMIN })
  public Account account(Long id) {
    return _accountRepository.findById(id);
  }

  @Query
  @Description("Login with PIN")
  public String pinLogin(PinLogin login) {
    return _accountService.pinLogin(login);
  }

  @Mutation
  @Description("Sign up with OIDC provider token and PIN")
  @RolesAllowed(Roles.USER)
  public Account signUp(String pin) {
    return _accountService.signUp(pin, _jwt);
  }

  @Mutation
  @Description("Update Account")
  @RolesAllowed(Roles.ADMIN)
  public Account updateAccount(Account account) {
    return _accountService.updateAccount(account);
  }

  @Mutation
  @Description("Delete Account")
  @RolesAllowed(Roles.ADMIN)
  @Transactional
  public Boolean deleteAccount(Long id) {
    return Boolean.valueOf(_accountRepository.deleteById(id));
  }

  @Mutation
  @Description("Set new PIN")
  @RolesAllowed(Roles.USER)
  public Boolean setPin(String pin) {
    return _accountService.setPin(pin, _jwt);
  }
}
