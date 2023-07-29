package de.keyruu.nexcalimat.graphql;

import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PagePojo;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.graphql.pojo.SortPojo;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GraphQLApi
public class AccountResource
{
	@Inject
	AccountService _accountService;

	@Inject
	JwtUtils _jwtUtils;

	@Inject
	JsonWebToken _jwt;

	@Query
	@Description("Get all Accounts")
	public PaginationResponse<Account> accounts(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return _accountService.listAll(Mapper.map(page, sort));
	}

	@Query
	@Description("Get Account by ID")
	public Account account(Long id)
	{
		return _accountService.findById(id);
	}

	@Query
	@Description("Get my account")
	@RolesAllowed(Roles.USER)
	public Account myAccount()
	{
		return _accountService.findByExtId(_jwtUtils.getExtIdFromToken(_jwt));
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
	public PaginationResponse<Account> deletedAccounts(Optional<PagePojo> page, Optional<SortPojo> sort)
	{
		return _accountService.getDeletedAccounts(Mapper.map(page, sort));
	}

	@Mutation
	@Description("Erase an Account permanently")
	@RolesAllowed(Roles.ADMIN)
	public Boolean eraseAccount(Long id)
	{
		return _accountService.eraseAccount(id);
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
	@Description("Delete Account")
	@RolesAllowed(Roles.ADMIN)
	@Transactional
	public Boolean deleteAccount(Long id)
	{
		return _accountService.deleteById(id);
	}

	@Mutation
	@Description("Set new PIN")
	@RolesAllowed(Roles.USER)
	public Boolean setPin(String pin)
	{
		return _accountService.setPin(pin, _jwtUtils.getExtIdFromToken(_jwt));
	}
}
