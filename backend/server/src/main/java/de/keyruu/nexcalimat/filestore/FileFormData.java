package de.keyruu.nexcalimat.filestore;

import java.io.File;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileFormData
{
	@NotNull
	@FormParam("file")
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
	public File file;

	@NotEmpty
	@FormParam("filename")
	@PartType(MediaType.TEXT_PLAIN)
	public String filename;
}