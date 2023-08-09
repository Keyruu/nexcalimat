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
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AccountService
{
	@Inject
	AccountRepository _accountRepo;

	@Inject
	JwtUtils _jwtUtils;

	@Inject
	PictureService _pictureService;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.name")
	String _nameClaim;

	@ConfigProperty(name = "de.keyruu.nexcalimat.claim.email")
	String _emailClaim;

	public PaginationResponse<Account> listAll(Mapper mapper, Optional<String> searchByName)
	{
		String query = "deletedAt IS NULL";
		if (searchByName.isPresent())
		{
			query += " AND LOWER(name) LIKE '%" + searchByName.get().toLowerCase() + "%'";
		}
		List<Account> activeAccounts = _accountRepo.find(query, mapper.getSort()).page(mapper.getPage()).list();
		long count = _accountRepo.count(query);
		return new PaginationResponse<>(activeAccounts, count, mapper);
	}

	public Account findById(Long id)
	{
		return _accountRepo.findById(id);
	}

	public Account findByExtId(String extId)
	{
		return _accountRepo.find("extId", extId).firstResultOptional().orElseThrow(AccountNotFoundException::new);
	}

	@Transactional
	public Account signUp(String pin, JsonWebToken idToken)
	{
		validatePin(pin);

		Account account = new Account();

		String extId = _jwtUtils.getExtIdFromToken(idToken);

		if (_accountRepo.find("extId", extId).firstResultOptional().isPresent())
		{
			throw new AccountExistsException();
		}

		account.setExtId(extId);
		account.setEmail((String)idToken.claim(_emailClaim).get());
		account.setName((String)idToken.claim(_nameClaim).get());
		account.setBalance(0L);
		account.setPinHash(BcryptUtil.bcryptHash(pin));

		_accountRepo.persist(account);

		return account;
	}

	public String pinLogin(PinLogin login)
	{
		Account account = _accountRepo.findByIdOptional(login.getId()).orElseThrow(AccountNotFoundException::new);

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

		Account account = _accountRepo.find("extId", extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		account.setPinHash(BcryptUtil.bcryptHash(pin));

		_accountRepo.persist(account);

		return Boolean.TRUE;
	}

	@Transactional
	public Account updateAccountPicture(Long id, FileFormData formData) throws IOException
	{
		Account dbAccount = _accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		return _pictureService.updatePicture(dbAccount, formData, _accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public Account updateMyAccountPicture(String extId, FileFormData formData) throws IOException
	{
		Account dbAccount = _accountRepo.find("extId", extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		return _pictureService.updatePicture(dbAccount, formData, _accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public void deleteAccountPicture(Long id)
	{
		Account dbAccount = _accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		_pictureService.deletePicture(dbAccount, _accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public void deleteMyAccountPicture(String extId)
	{
		Account dbAccount = _accountRepo.find("extId", extId).firstResultOptional()
			.orElseThrow(AccountNotFoundException::new);

		_pictureService.deletePicture(dbAccount, _accountRepo, PictureType.ACCOUNT);
	}

	@Transactional
	public Account updateAccount(Account account)
	{
		Account dbAccount = _accountRepo.findByIdOptional(account.getId())
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

		_accountRepo.persist(dbAccount);

		return account;
	}

	@Transactional
	public Boolean reactivateAccount(Long id)
	{
		Account dbAccount = _accountRepo.findByIdOptional(id)
			.orElseThrow(AccountNotFoundException::new);

		dbAccount.setDeletedAt(null);
		_accountRepo.persist(dbAccount);
		return true;
	}

	@Transactional
	public Boolean eraseAccount(Long id)
	{
		return _accountRepo.deleteById(id);
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
		Account account = _accountRepo.findByIdOptional(id).orElseThrow(AccountNotFoundException::new);

		account.setDeletedAt(LocalDateTime.now());
		_accountRepo.persist(account);

		return Boolean.TRUE;
	}

	public PaginationResponse<Account> getDeletedAccounts(Mapper mapper)
	{
		String query = "deletedAt IS NOT NULL";
		List<Account> deletedAccounts = _accountRepo.find(query, mapper.getSort()).page(mapper.getPage()).list();
		long count = _accountRepo.count(query);
		return new PaginationResponse<>(deletedAccounts, count, mapper);
	}
}
