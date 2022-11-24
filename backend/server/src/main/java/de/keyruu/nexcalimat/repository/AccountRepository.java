package de.keyruu.nexcalimat.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.exception.AccountExistsException;
import de.keyruu.nexcalimat.graphql.exception.PinValidationException;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.PinInput;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.user-id")
  String userIdClaim;

  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.name")
  String nameClaim;

  @ConfigProperty(name = "de.keyruu.nexcalimat.claim.email")
  String emailClaim;

  @Transactional
  public Account signUp(PinInput pinInput, JsonWebToken idToken) {
    if (!isNumeric(pinInput.getPin()) || pinInput.getPin().length() != 4) {
      throw new PinValidationException();
    }

    Account account = new Account();

    String extId = (String) idToken.claim(userIdClaim).get();

    if (find("extId", extId).firstResultOptional().isPresent()) {
      throw new AccountExistsException();
    }

    account.setExtId(extId);
    account.setEmail((String) idToken.claim(emailClaim).get());
    account.setName((String) idToken.claim(nameClaim).get());
    account.setBalance(0);
    account.setPinHash(BcryptUtil.bcryptHash(pinInput.getPin()));

    persist(account);

    return account;
  }

  public static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}