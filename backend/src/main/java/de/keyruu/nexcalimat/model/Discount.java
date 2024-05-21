package de.keyruu.nexcalimat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Discount
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Min(0)
	@Max(100)
	@Column(name = "percentage", nullable = false)
	private Integer percentage;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getPercentage()
	{
		return percentage;
	}

	public void setPercentage(Integer percentage)
	{
		this.percentage = percentage;
	}
}
