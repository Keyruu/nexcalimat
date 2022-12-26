package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;

@GraphQLApi
public class AccountResource
{
  @Inject
  AccountRepository _accountRepository;

  @Inject
  AccountService _accountService;

  @Inject
  JwtUtils _jwtUtils;

  @Inject
  JsonWebToken _jwt;

  @Query
  @Description("Get all Accounts")
  public List<Account> accounts()
  {
    return _accountRepository.listAll();
  }

  @Query
  @Description("Get Account by ID")
  public Account account(Long id)
  {
    return _accountRepository.findById(id);
  }

  @Query
  @Description("Login with PIN")
  public String pinLogin(PinLogin login)
  {
    return _accountService.pinLogin(login);
  }

  @Query
  @Description("Get deleted Accounts")
  @RolesAllowed(Roles.ADMIN)
  public List<Account> deletedAccounts()
  {
    return _accountRepository.getEntityManager()
      .createNamedQuery("deletedAccounts", Account.class)
      .getResultList();
  }

  @Mutation
  @Description("Erase an Account permanently")
  @RolesAllowed(Roles.ADMIN)
  public Boolean eraseAccount(Long id)
  {
    return _accountService.eraseAccountData(id);
  }

  @Mutation
  @Description("Sign up with OIDC provider token and PIN")
  @RolesAllowed(Roles.USER)
  public Account signUp(String pin)
  {
    return _accountService.signUp(pin, _jwt);
  }

  @Mutation
  @Description("Update Account")
  @RolesAllowed(Roles.ADMIN)
  public Account updateAccount(Account account)
  {
    return _accountService.updateAccount(account);
  }

  @Mutation
  @Description("Update my Account Picture")
  @RolesAllowed({ Roles.USER, Roles.ADMIN })
  public Account updateMyAccountPicture(String picture)
  {
    return _accountService.updateMyAccountPicture(picture, _jwtUtils.getExtIdFromToken(_jwt));
  }

  @Mutation
  @Description("Update Account Picture")
  @RolesAllowed(Roles.ADMIN)
  public Account updateAccountPicture(Account account)
  {
    return _accountService.updateAccountPicture(account);
  }

  @Mutation
  @Description("Delete Account")
  @RolesAllowed(Roles.ADMIN)
  @Transactional
  public Boolean deleteAccount(Long id)
  {
    return Boolean.valueOf(_accountRepository.deleteById(id));
  }

  @Mutation
  @Description("Set new PIN")
  @RolesAllowed(Roles.USER)
  public Boolean setPin(String pin)
  {
    return _accountService.setPin(pin, _jwtUtils.getExtIdFromToken(_jwt));
  }
}
