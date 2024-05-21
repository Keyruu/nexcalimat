package de.keyruu.nexcalimat.configuration;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "de.keyruu.nexcalimat.groups")
public interface GroupsConfiguration
{
	String userGroupName();

	String adminGroupName();

	String discountedGroupName();
}
