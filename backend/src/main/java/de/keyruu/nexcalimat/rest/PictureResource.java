package de.keyruu.nexcalimat.rest;

import java.io.IOException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import de.keyruu.nexcalimat.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/v1/picture")
public class PictureResource
{
	@Inject
	AccountService accountService;

	@Inject
	ProductService productService;

	@Inject
	JsonWebToken jwt;

	@Inject
	JwtUtils jwtUtils;

	@POST
	@Path("/account/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed(Roles.ADMIN)
	public Response updateAccountPicture(@PathParam("id") Long id,
		@MultipartForm @Valid FileFormData formData) throws IOException
	{
		accountService.updateAccountPicture(id, formData);
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("/account/{id}")
	@RolesAllowed(Roles.ADMIN)
	public Response deleteAccountPicture(@PathParam("id") Long id)
	{
		accountService.deleteAccountPicture(id);
		return Response.status(Status.NO_CONTENT).build();
	}

	@POST
	@Path("/myAccount")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed(Roles.USER)
	public Response updateMyAccountPicture(@MultipartForm @Valid FileFormData formData) throws IOException
	{
		accountService.updateMyAccountPicture(jwtUtils.getExtIdFromToken(jwt), formData);
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("/myAccount")
	@RolesAllowed(Roles.USER)
	public Response deleteMyAccountPicture()
	{
		accountService.deleteMyAccountPicture(jwtUtils.getExtIdFromToken(jwt));
		return Response.status(Status.NO_CONTENT).build();
	}

	@POST
	@Path("/product/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed(Roles.ADMIN)
	public Response updateProductPicture(@PathParam("id") Long id,
		@MultipartForm @Valid FileFormData formData) throws IOException
	{
		productService.updateProductPicture(id, formData);
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("/product/{id}")
	@RolesAllowed(Roles.ADMIN)
	public Response deleteProductPicture(@PathParam("id") Long id)
	{
		productService.deleteProductPicture(id);
		return Response.status(Status.NO_CONTENT).build();
	}
}