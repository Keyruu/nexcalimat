package de.keyruu.nexcalimat.graphql.pojo;

public class PagePojo
{
	private Integer index;
	private Integer size = 30;

	public Integer getIndex()
	{
		return this.index;
	}

	public void setIndex(Integer index)
	{
		this.index = index;
	}

	public Integer getSize()
	{
		return this.size;
	}

	public void setSize(Integer size)
	{
		this.size = size;
	}
}
