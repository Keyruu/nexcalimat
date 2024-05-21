package de.keyruu.nexcalimat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.PictureType;
import de.keyruu.nexcalimat.graphql.exception.AccountExistsException;
import de.keyruu.nexcalimat.graphql.exception.AccountNotFoundException;
import de.keyruu.nexcalimat.graphql.exception.IllegalOperationException;
import de.keyruu.nexcalimat.graphql.exception.PinValidationException;
import de.keyruu.nexcalimat.graphql.exception.WrongPinException;
import de.keyruu.nexcalimat.graphql.pojo.Mapper;
import de.keyruu.nexcalimat.graphql.pojo.PaginationResponse;
import de.keyruu.nexcalimat.graphql.pojo.PinLogin;
import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AccountService
{
	private static final String EXT_ID = "extId";

	@Inject
	AccountRepository accountRepo;

	@Inject
	JwtUtils jwtUtils;

	@Inject
	PictureService pictureService;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.name")
	String nameClaim;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.email")
	String emailClaim;

	public PaginationResponse<Account> listAll(Mapper mapper, Optional<String> searchByName)
	{
		String query = "deletedAt IS NULL";
		PanacheQuery<Account> panacheQuery;
		long count;
		if (searchByName.isPresent())
		{
			query += " AND LOWER(name) LIKE :name";
			Parameters parameters = Parameters.with("name", "%" + searchByName.get().toLowerCase() + "%");

			panacheQuery = accountRepo.find(query, mapper.getSort(), parameters);
			count = accountRepo.count(query, parameters);
		}
		else
		{
			panacheQuery = accountRepo.find(query, mapper.getSort());
			count = accountRepo.count(query);
		}
		List<Account> activeAccounts = panacheQuery.page(mapper.getPage()).list();
		return new PaginationResponse<>(activeAccounts, count, mapper);
	}

	public Account findById(Long id)
	{
		return accountRepo.findById(id);
	}

	public Account findByExtId(String extId)
	{
		return accountRepo.find(EXT_ID, extId).firstResultOptional().orElseThrow(AccountNotFoundException::new);
	}

	@Transactional
	public Account signUp(String pin, JsonWebToken idToken)
	{
		validatePin(pin);

		Account account = new Account();

		String extId = jwtUtils.getExtIdFromToken(idToken);

		if (accountRepo.find(EXT_ID, extId).firstResultOptional().isPresent())
		{
			throw new AccountExistsException();
		}

		account.setExtId(extId);
		idToken.claim(emailClaim).ifPresent(email -> account.setEmail((String)email));
		idToken.claim(nameClaim).ifPresent(name -> account.setName((String)name));
		account.setBalance(0L);
		account.setPinHash(BcryptUtil.bcryptHash(pin));

		accountRepo.persist(account);

		return account;
	}

	public String pinLogin(PinLogin login)
	{
		Account account = accountRepo.findByIdOptional(login.getId()).orElseThrow(AccountNotFoundException::new);

		if (!BcryptUtil.matches(login.getPin(), account.getPinHash()))
		{
			throw new WrongPinException();
		}

		return Jwt.upn(account.getId().toString()).groups(Roles.CUSTOMER).sign();
	}

	@Transactional
	public Boolean setPin(String pin, String extId)
	{
		validatePin(pin);

		Account account = accountRepo.find(EXT_ID, extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		account.setPinHash(BcryptUtil.bcryptHash(pin));

		accountRepo.persist(account);

		return Boolean.TRUE;
	}

	@Transactional
	public Account updateAccountPicture(Long id, FileFormData formData) throws IOException
	{
		Account dbAccount = accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		return pictureService.updatePicture(dbAccount, formData, accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public Account updateMyAccountPicture(String extId, FileFormData formData) throws IOException
	{
		Account dbAccount = accountRepo.find(EXT_ID, extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		return pictureService.updatePicture(dbAccount, formData, accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public void deleteAccountPicture(Long id)
	{
		Account dbAccount = accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		pictureService.deletePicture(dbAccount, accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public void deleteMyAccountPicture(String extId)
	{
		Account dbAccount = accountRepo.find(EXT_ID, extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		pictureService.deletePicture(dbAccount, accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public Account updateAccount(Account account)
	{
		Account dbAccount = accountRepo.findByIdOptional(account.getId())
			.orElseThrow(AccountNotFoundException::new);

		if (account.getBalance() != null)
		{
			dbAccount.setBalance(account.getBalance());
		}
		if (account.getName() != null)
		{
			dbAccount.setName(account.getName());
		}
		if (account.getExtId() != null)
		{
			dbAccount.setExtId(account.getExtId());
		}
		if (account.getEmail() != null)
		{
			dbAccount.setEmail(account.getEmail());
		}

		accountRepo.persist(dbAccount);

		return account;
	}

	@Transactional
	public Boolean reactivateAccount(Long id)
	{
		Account dbAccount = accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		if (dbAccount.getDeletedAt() == null)
		{
			throw new IllegalOperationException();
		}

		dbAccount.setDeletedAt(null);
		accountRepo.persist(dbAccount);
		return true;
	}

	@Transactional
	public Boolean eraseAccount(Long id)
	{
		return accountRepo.deleteById(id);
	}

	public static void validatePin(String pin)
	{
		if (!isNumeric(pin) || pin.length() != 4)
		{
			throw new PinValidationException();
		}
	}

	public static boolean isNumeric(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Transactional
	public Boolean deleteById(Long id)
	{
		Account account = accountRepo.findByIdOptional(id).orElseThrow(AccountNotFoundException::new);

		if (account.getDeletedAt() != null)
		{
			throw new IllegalOperationException();
		}

		account.setDeletedAt(LocalDateTime.now());
		accountRepo.persist(account);

		return Boolean.TRUE;
	}

	public PaginationResponse<Account> getDeletedAccounts(Mapper mapper)
	{
		String query = "deletedAt IS NOT NULL";
		List<Account> deletedAccounts = accountRepo.find(query, mapper.getSort()).page(mapper.getPage()).list();
		long count = accountRepo.count(query);
		return new PaginationResponse<>(deletedAccounts, count, mapper);
	}
}