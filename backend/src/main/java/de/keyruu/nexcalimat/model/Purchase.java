package de.keyruu.nexcalimat.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Purchase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "paid_price", nullable = false)
	private Integer paidPrice;

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalDateTime getCreatedAt()
	{
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt)
	{
		this.createdAt = createdAt;
	}

	public LocalDateTime getDeletedAt()
	{
		return this.deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt)
	{
		this.deletedAt = deletedAt;
	}

	public Account getAccount()
	{
		return this.account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public Product getProduct()
	{
		return this.product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Integer getPaidPrice()
	{
		return this.paidPrice;
	}

	public void setPaidPrice(Integer paidPrice)
	{
		this.paidPrice = paidPrice;
	}
}
