package de.keyruu.nexcalimat;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.Purchase;
import de.keyruu.nexcalimat.repository.AccountRepository;
import de.keyruu.nexcalimat.repository.FavoriteRepository;
import de.keyruu.nexcalimat.repository.ProductRepository;
import de.keyruu.nexcalimat.repository.PurchaseRepository;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import de.keyruu.nexcalimat.service.FavoriteService;
import de.keyruu.nexcalimat.service.PurchaseService;
import de.keyruu.nexcalimat.utils.TestUtils;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public abstract class NexcalimatTest
{
	protected Account dubinsky = TestUtils.dubinsky();
	protected Account hai = TestUtils.hai();
	protected Account even = TestUtils.even();
	protected Product peitsche = TestUtils.peitsche();
	protected Product yoyo = TestUtils.yoyo();
	protected Product maske = TestUtils.maske();
	protected Purchase purchase1 = TestUtils.purchase(dubinsky, peitsche, 7000);
	protected Purchase purchase2 = TestUtils.purchase(even, peitsche, 5000);
	protected Purchase purchase3 = TestUtils.purchase(even, yoyo, 80);
	protected Purchase purchase4 = TestUtils.purchase(dubinsky, peitsche, 9000);
	protected Purchase expiredPurchase = TestUtils.purchase(dubinsky, yoyo, 90);

	@Inject
	AccountService accountService;

	@Inject
	AccountRepository accountRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	PurchaseService purchaseService;

	@Inject
	PurchaseRepository purchaseRepository;

	@Inject
	FavoriteService favoriteService;

	@Inject
	FavoriteRepository favoriteRepository;

	protected String getEarlsToken()
	{
		return getOidcToken("earl", Set.of("some-random-admin-group-name"), "", "Earl of Cockwood");
	}

	protected String getDubinskysToken()
	{
		return getOidcToken("dubinsky", Set.of("some-random-user-group-name"), "dieter@dubinsky.de", "Dieter Dubinsky");
	}

	@BeforeEach
	@Transactional
	protected void testData()
	{
		accountRepository.persist(dubinsky, even, hai);
		productRepository.persist(peitsche, yoyo, maske);
		favoriteService.toggleFavorite(peitsche.getId(), even.getId());
		favoriteService.toggleFavorite(maske.getId(), dubinsky.getId());
		purchaseRepository.persist(purchase1, purchase2, purchase3, purchase4);
		accountService.deleteById(hai.getId());
		purchaseService.refund(purchase4.getId(), purchase4.getAccount().getId());
		purchaseRepository.persist(expiredPurchase);
		purchaseRepository.getEntityManager()
			.createNativeQuery("UPDATE purchase SET created_at = :created WHERE id = :id")
			.setParameter("created", LocalDateTime.now().minusMinutes(10))
			.setParameter("id", expiredPurchase.getId())
			.executeUpdate();
	}

	@AfterEach
	@Transactional
	protected void delete()
	{
		favoriteRepository.deleteAll();
		purchaseRepository.deleteAll();
		accountRepository.deleteAll();
		productRepository.deleteAll();
	}

	protected String getOidcToken(String userName, Set<String> groups, String email, String name)
	{
		return Jwt.preferredUserName(userName)
			.groups(groups)
			.issuer("https://server.example.com")
			.audience("https://service.example.com")
			.claim("sub", userName)
			.claim("email", email)
			.claim("name", name)
			.sign();
	}

	protected String getPinToken(Long userId)
	{
		return Jwt.upn(userId.toString())
			.groups(Roles.CUSTOMER)
			.sign("privateKey.pem");
	}
}
