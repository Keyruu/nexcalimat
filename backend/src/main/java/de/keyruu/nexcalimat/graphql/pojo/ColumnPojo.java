package de.keyruu.nexcalimat.graphql.pojo;

public class ColumnPojo
{
  private String name;
  private DirectionPojo direction = DirectionPojo.Ascending;

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public DirectionPojo getDirection()
  {
    return this.direction;
  }

  public void setDirection(DirectionPojo direction)
  {
    this.direction = direction;
  }
}