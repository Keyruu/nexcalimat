package de.keyruu.nexcalimat.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.eclipse.microprofile.graphql.Ignore;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Account implements HasPicture
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "ext_id", nullable = false, unique = true)
	private String extId;

	@Email
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long balance;

	@Size(max = 3000)
	@Column(length = 3000)
	private String picture;

	@Column(name = "pin_hash")
	@Ignore
	private String pinHash;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@Ignore
	private Set<Purchase> purchases = new HashSet<>();

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

	public String getExtId()
	{
		return this.extId;
	}

	public void setExtId(String extId)
	{
		this.extId = extId;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getBalance()
	{
		return this.balance;
	}

	public void setBalance(Long balance)
	{
		this.balance = balance;
	}

	@Override
	public String getPicture()
	{
		return this.picture;
	}

	@Override
	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getPinHash()
	{
		return this.pinHash;
	}

	public void setPinHash(String pinHash)
	{
		this.pinHash = pinHash;
	}

	public Set<Purchase> getPurchases()
	{
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases)
	{
		this.purchases = purchases;
	}
}
