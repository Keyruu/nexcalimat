package de.keyruu.nexcalimat.graphql;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.repository.AccountRepository;

@GraphQLApi
public class AccountResource {
  @Inject
  AccountRepository _accountRepository;

  @Query
  @Description("Say hello")
  public List<Account> accounts() {
    return _accountRepository.listAll();
  }

  @Mutation
  @Description("")
  public Account account(Account accountInput) {
    _accountRepository.persist(accountInput);
    return accountInput;
  }
}
