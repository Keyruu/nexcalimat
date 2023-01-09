package de.keyruu.nexcalimat.filestore;

import java.io.File;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

public class FileFormData
{
	@NotNull
	@RestForm("file")
	public File data;

	@NotEmpty
	@RestForm
	@PartType(MediaType.TEXT_PLAIN)
	public String filename;
}