package de.keyruu.nexcalimat.service.pojo;

import java.time.LocalDateTime;

import de.keyruu.nexcalimat.model.HasPicture;
import de.keyruu.nexcalimat.model.Product;
import de.keyruu.nexcalimat.model.ProductType;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductWithFavorite implements HasPicture
{
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime deletedAt;
  private String name;
  private String picture;
  private Integer price;
  private Integer bundleSize;
  private ProductType type;
  private Boolean isFavorite;

  public ProductWithFavorite()
  {
  }

  public ProductWithFavorite(Product product, Long fId)
  {
    this.id = product.getId();
    this.createdAt = product.getCreatedAt();
    this.deletedAt = product.getDeletedAt();
    this.name = product.getName();
    this.picture = product.getPicture();
    this.price = product.getPrice();
    this.bundleSize = product.getBundleSize();
    this.type = product.getType();
    this.isFavorite = fId != 0;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public LocalDateTime getCreatedAt()
  {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt)
  {
    this.createdAt = createdAt;
  }

  public LocalDateTime getDeletedAt()
  {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt)
  {
    this.deletedAt = deletedAt;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPicture()
  {
    return picture;
  }

  public void setPicture(String picture)
  {
    this.picture = picture;
  }

  public Integer getPrice()
  {
    return price;
  }

  public void setPrice(Integer price)
  {
    this.price = price;
  }

  public Integer getBundleSize()
  {
    return bundleSize;
  }

  public void setBundleSize(Integer bundleSize)
  {
    this.bundleSize = bundleSize;
  }

  public ProductType getType()
  {
    return type;
  }

  public void setType(ProductType type)
  {
    this.type = type;
  }

  public Boolean getIsFavorite()
  {
    return isFavorite;
  }

  public void setIsFavorite(Boolean isFavorite)
  {
    this.isFavorite = isFavorite;
  }
}
