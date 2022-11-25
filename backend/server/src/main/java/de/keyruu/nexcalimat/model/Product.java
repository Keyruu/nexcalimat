package de.keyruu.nexcalimat.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE account SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NOT NULL")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String picture;

  @Column(nullable = false)
  private Integer price;

  @Column(name = "bundle_size", nullable = false)
  private Integer bundleSize;

  @Column(nullable = false)
  private ProductType type;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getDeletedAt() {
    return this.deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPicture() {
    return this.picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Integer getPrice() {
    return this.price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getBundleSize() {
    return this.bundleSize;
  }

  public void setBundleSize(Integer bundleSize) {
    this.bundleSize = bundleSize;
  }

  public ProductType getType() {
    return this.type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }
}
