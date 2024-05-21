package de.keyruu.nexcalimat.graphql;

import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.MyAccount;
import de.keyruu.nexcalimat.graphql.pojo.PagePojo;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.graphql.pojo.SortPojo;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GraphQLApi
public class AccountResource
{
	@Inject
	AccountService accountService;

	@Inject
	JwtUtils jwtUtils;

	@Inject
	JsonWebToken jwt;

	@Inject
	SecurityIdentity identity;

	@Query
	@Description("Get all Accounts")
	public PaginationResponse<Account> accounts(Optional<PagePojo> page, Optional<SortPojo> sort, Optional<String> searchByName)
	{
		return accountService.listAll(Mapper.map(page, sort), searchByName);
	}

	@Query
	@Description("Get Account by ID")
	public Account account(Long id)
	{
		return accountService.findById(id);
	}

	@Query
	@Description("Get my account")
	@RolesAllowed(Roles.USER)
	public MyAccount myAccount()
	{
		boolean isAdmin = identity.getRoles().contains(Roles.ADMIN);
		Account account = accountService.findByExtId(jwtUtils.getExtIdFromToken(jwt));
		return new MyAccount(account, isAdmin);
	}

	@Query
	@Description("Login with PIN")
	public String pinLogin(PinLogin login)
	{
		return accountService.pinLogin(login);
	}

	@Query
	@Description("Get deleted Accounts")
	@RolesAllowed(Roles.ADMIN)
	public PaginationResponse<Account> deletedAccounts(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return accountService.getDeletedAccounts(Mapper.map(page, sort));
	}

	@Mutation
	@Description("Erase an Account permanently")
	@RolesAllowed(Roles.ADMIN)
	public Boolean eraseAccount(Long id)
	{
		return accountService.eraseAccount(id);
	}

	@Mutation
	@Description("Reactivate an Account")
	@RolesAllowed(Roles.ADMIN)
	public Boolean reactivateAccount(Long id)
	{
		return accountService.reactivateAccount(id);
	}

	@Mutation
	@Description("Sign up with OIDC provider token and PIN")
	@RolesAllowed(Roles.USER)
	public Account signUp(String pin)
	{
		return accountService.signUp(pin, jwt);
	}

	@Mutation
	@Description("Update Account")
	@RolesAllowed(Roles.ADMIN)
	public Account updateAccount(Account account)
	{
		return accountService.updateAccount(account);
	}

	@Mutation
	@Description("Delete Account")
	@RolesAllowed(Roles.ADMIN)
	@Transactional
	public Boolean deleteAccount(Long id)
	{
		return accountService.deleteById(id);
	}

	@Mutation
	@Description("Set new PIN")
	@RolesAllowed(Roles.USER)
	public Boolean setPin(String pin)
	{
		return accountService.setPin(pin, jwtUtils.getExtIdFromToken(jwt));
	}
}
