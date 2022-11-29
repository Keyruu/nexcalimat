package de.keyruu.nexcalimat.utils;

import de.keyruu.nexcalimat.model.Account;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import de.keyruu.nexcalimat.model.Purchase;
import io.quarkus.elytron.security.common.BcryptUtil;

public class TestUtils {
  public static Account dubinsky() {
    Account a = new Account();
    a.setEmail("dubinsky@keyruu.de");
    a.setName("Dieter Dubinsky");
    a.setBalance(0L);
    a.setExtId("dubinsky");
    a.setPinHash(BcryptUtil.bcryptHash("0000"));
    return a;
  }

  public static Account even() {
    Account a = new Account();
    a.setEmail("even@keyruu.de");
    a.setName("Even Longer");
    a.setBalance(0L);
    a.setExtId("even");
    a.setPinHash(BcryptUtil.bcryptHash("0000"));
    return a;
  }

  public static Product peitsche() {
    Product p = new Product();
    p.setBundleSize(6);
    p.setName("Die Peitsche des Mönchs");
    p.setPrice(6000);
    p.setType(ProductType.COLD_DRINK);
    return p;
  }

  public static Product yoyo() {
    Product p = new Product();
    p.setBundleSize(20);
    p.setName("Das Yoyo von Long");
    p.setPrice(80);
    p.setType(ProductType.HOT_DRINK);
    return p;
  }

  public static Purchase purchase(Account a, Product pr, Integer paidPrice) {
    Purchase p = new Purchase();
    p.setAccount(a);
    p.setProduct(pr);
    p.setPaidPrice(paidPrice);
    return p;
  }
}
