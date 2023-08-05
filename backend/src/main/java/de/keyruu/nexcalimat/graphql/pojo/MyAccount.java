package de.keyruu.nexcalimat.graphql.pojo;

import de.keyruu.nexcalimat.model.Account;

public class MyAccount
{
  private Account account;
  private Boolean isAdmin;

  public MyAccount(Account account, Boolean isAdmin)
  {
    this.account = account;
    this.isAdmin = isAdmin;
  }

  public Account getAccount()
  {
    return account;
  }

  public void setAccount(Account account)
  {
    this.account = account;
  }

  public Boolean getIsAdmin()
  {
    return isAdmin;
  }

  public void setIsAdmin(Boolean isAdmin)
  {
    this.isAdmin = isAdmin;
  }
}
