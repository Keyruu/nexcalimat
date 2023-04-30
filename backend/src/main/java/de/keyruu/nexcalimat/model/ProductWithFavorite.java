package de.keyruu.nexcalimat.model;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "product_with_favorite")
public class ProductWithFavorite extends SimpleProduct
{
  @Column(name = "is_favorite")
  private Boolean isFavorite;

  @Column(name = "account_id")
  private Long accountId;

  public Boolean getIsFavorite()
  {
    return isFavorite;
  }

  public void setIsFavorite(Boolean isFavorite)
  {
    this.isFavorite = isFavorite;
  }

  public Long getAccountId()
  {
    return accountId;
  }

  public void setAccountId(Long accountId)
  {
    this.accountId = accountId;
  }
}