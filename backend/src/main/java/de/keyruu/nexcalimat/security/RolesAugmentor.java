package de.keyruu.nexcalimat.security;

import java.util.function.Supplier;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor
{
	@ConfigProperty(name = "de.keyruu.nexcalimat.userGroupName")
	String _userGroupName;

	@ConfigProperty(name = "de.keyruu.nexcalimat.adminGroupName")
	String _adminGroupName;

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

		if (identity.getRoles().contains(_userGroupName))
		{
			builder.addRole(Roles.USER);
		}

		if (identity.getRoles().contains(_adminGroupName))
		{
			builder.addRole(Roles.USER);
			builder.addRole(Roles.ADMIN);
		}

		return builder::build;
	}
}
