package de.keyruu.nexcalimat.repository;

import javax.enterprise.context.ApplicationScoped;

import de.keyruu.nexcalimat.model.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account>
{
}
