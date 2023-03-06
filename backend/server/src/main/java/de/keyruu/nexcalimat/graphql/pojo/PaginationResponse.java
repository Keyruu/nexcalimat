package de.keyruu.nexcalimat.graphql.pojo;

import java.util.List;

public class PaginationResponse<T>
{
    private List<T> data;
    private long total;
    private int page;

    public PaginationResponse(List<T> data, long total, Mapper mapper)
    {
        this.data = data;
        this.total = total;
        this.page = mapper.getPage().index;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }
}
