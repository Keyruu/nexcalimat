package de.keyruu.nexcalimat.graphql.pojo;

import java.util.Optional;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

public class Mapper
{
  private Page page;
  private Sort sort;

  public static Mapper map(Optional<PagePojo> pagePojoOptional, Optional<SortPojo> sortPojoOptional)
  {
    return map(pagePojoOptional, sortPojoOptional, Sort.empty(), "");
  }

  public static Mapper map(Optional<PagePojo> pagePojoOptional, Optional<SortPojo> sortPojoOptional, Sort initialSort, String prefix)
  {
    Mapper mapper = new Mapper();
    mapper.setPage(mapPage(pagePojoOptional));
    mapper.setSort(mapSort(sortPojoOptional, initialSort, prefix));
    return mapper;
  }

  private static Page mapPage(Optional<PagePojo> pagePojoOptional)
  {
    Page page = Page.of(0, 30);
    if (pagePojoOptional.isPresent())
    {
      var pagePojo = pagePojoOptional.get();
      page = Page.of(pagePojo.getIndex(), pagePojo.getSize());
    }
    return page;
  }

  private static Sort mapSort(Optional<SortPojo> sortPojoOptional, Sort initialSort, String prefix)
  {
    Sort sort = initialSort;
    if (sortPojoOptional.isPresent())
    {
      var sortPojo = sortPojoOptional.get();
      for (ColumnPojo column : sortPojo.getColumns())
      {
        Direction direction;
        if (column.getDirection().equals(DirectionPojo.Ascending))
        {
          direction = Direction.Ascending;
        }
        else
        {
          direction = Direction.Descending;
        }
        sort = sort.and(prefix + column.getName(), direction);
      }
    }
    return sort;
  }

  public Page getPage()
  {
    return this.page;
  }

  public void setPage(Page page)
  {
    this.page = page;
  }

  public Sort getSort()
  {
    return this.sort;
  }

  public void setSort(Sort sort)
  {
    this.sort = sort;
  }

}
