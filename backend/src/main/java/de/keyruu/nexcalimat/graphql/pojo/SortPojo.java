package de.keyruu.nexcalimat.graphql.pojo;

import java.util.ArrayList;
import java.util.List;

public class SortPojo
{
	private List<ColumnPojo> columns = new ArrayList<>();

	public List<ColumnPojo> getColumns()
	{
		return this.columns;
	}

	public void setColumns(List<ColumnPojo> columns)
	{
		this.columns = columns;
	}
}
