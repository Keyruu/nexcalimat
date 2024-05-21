package de.keyruu.nexcalimat.repository;

import de.keyruu.nexcalimat.model.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account>
{
	//
}