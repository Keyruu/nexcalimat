package de.keyruu.nexcalimat.model.projection;

import de.keyruu.nexcalimat.model.Account;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountPurchaseCount
{
	private final Account account;
	private final long count;

	public AccountPurchaseCount(Account account, long count)
	{
		this.account = account;
		this.count = count;
	}

	public Account getAccount()
	{
		return account;
	}

	public long getCount()
	{
		return count;
	}
}
