package de.keyruu.nexcalimat.service;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.AccountExistsException;
import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.PinValidationException;
import de.keyruu.nexcalimat.graphql.exception.WrongPinException;
import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.security.Roles;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class AccountService {
  @Inject
  AccountRepository _accountRepo;

  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.user-id")
  String _userIdClaim;

  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.name")
  String _nameClaim;

  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.email")
  String _emailClaim;

  @Transactional
  public Account signUp(String pin, JsonWebToken idToken) {
    validatePin(pin);

    Account account = new Account();

    String extId = getExtIdFromToken(idToken);

    if (_accountRepo.find("extId", extId).firstResultOptional().isPresent()) {
      throw new AccountExistsException();
    }

    account.setExtId(extId);
    account.setEmail((String) idToken.claim(_emailClaim).get());
    account.setName((String) idToken.claim(_nameClaim).get());
    account.setBalance(0L);
    account.setPinHash(BcryptUtil.bcryptHash(pin));

    _accountRepo.persist(account);

    return account;
  }

  public String pinLogin(PinLogin login) {
    Account account = _accountRepo.findById(login.getId());
    if (account == null) {
      throw new AccountNotFoundException();
    }

    if (BcryptUtil.matches(login.getPin(), account.getPinHash()) == false) {
      throw new WrongPinException();
    }

    return Jwt.upn(account.getId().toString()).groups(Roles.CUSTOMER).sign();
  }

  @Transactional
  public Boolean setPin(String pin, JsonWebToken idToken) {
    validatePin(pin);

    Optional<Account> accountOptional = _accountRepo.find("extId", getExtIdFromToken(idToken)).firstResultOptional();

    if (accountOptional.isEmpty()) {
      throw new AccountNotFoundException();
    }

    Account account = accountOptional.get();

    account.setPinHash(BcryptUtil.bcryptHash(pin));

    _accountRepo.persist(account);

    return Boolean.TRUE;
  }

  @Transactional
  public Account updateAccount(Account account) {
    Optional<Account> dbAccountOptional = _accountRepo.findByIdOptional(account.getId());

    if (dbAccountOptional.isEmpty()) {
      throw new AccountNotFoundException();
    }

    Account dbAccount = dbAccountOptional.get();
    dbAccount.setBalance(account.getBalance());
    dbAccount.setPicture(account.getPicture());
    dbAccount.setName(account.getName());
    dbAccount.setExtId(account.getExtId());
    dbAccount.setEmail(account.getEmail());

    _accountRepo.persist(dbAccount);

    return account;
  }

  private String getExtIdFromToken(JsonWebToken idToken) {
    return (String) idToken.claim(_userIdClaim).get();
  }

  private void validatePin(String pin) {
    if (isNumeric(pin) == false || pin.length() != 4) {
      throw new PinValidationException();
    }
  }

  public static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
