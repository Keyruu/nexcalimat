package de.keyruu.nexcalimat.security;

import java.util.function.Supplier;

import de.keyruu.nexcalimat.configuration.GroupsConfiguration;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor
{
	@Inject
	GroupsConfiguration groupsConfiguration;

	@Override
	public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context)
	{
		return Uni.createFrom().item(build(identity));
	}

	private Supplier<SecurityIdentity> build(SecurityIdentity identity)
	{
		// create a new builder and copy principal, attributes, credentials and
		// roles
		// from the original identity
		QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);

		if (identity.getRoles().contains(groupsConfiguration.userGroupName()))
		{
			builder.addRole(Roles.USER);
		}

		if (identity.getRoles().contains(groupsConfiguration.adminGroupName()))
		{
			builder.addRole(Roles.USER);
			builder.addRole(Roles.ADMIN);
		}

		if (identity.getRoles().contains(groupsConfiguration.discountedGroupName()))
		{
			builder.addRole(Roles.DISCOUNTED);
		}

		return builder::build;
	}
}
