package de.keyruu.nexcalimat.rest;

import java.io.IOException;

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
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.FilestoreClient;
import de.keyruu.nexcalimat.security.JwtUtils;
import de.keyruu.nexcalimat.security.Roles;
import de.keyruu.nexcalimat.service.AccountService;
import de.keyruu.nexcalimat.service.ProductService;

@Path("/api/v1/picture")
public class PictureResource
{
  @Inject
  FilestoreClient _filestoreClient;

  @Inject
  AccountService _accountService;

  @Inject
  ProductService _productService;

  @Inject
  JsonWebToken _jwt;

  @Inject
  JwtUtils _jwtUtils;

  @POST
  @Path("/account/{id}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @RolesAllowed(Roles.ADMIN)
  public Response updateAccountPicture(@PathParam("id") Long id,
    @MultipartForm @Valid FileFormData formData) throws IOException
  {
    _accountService.updateAccountPicture(id, formData);
    return Response.status(Status.CREATED).build();
  }

  @DELETE
  @Path("/account/{id}")
  @RolesAllowed(Roles.ADMIN)
  public Response deleteAccountPicture(@PathParam("id") Long id)
  {
    _accountService.deleteAccountPicture(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Path("/myAccount")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @RolesAllowed(Roles.USER)
  public Response updateMyAccountPicture(@MultipartForm @Valid FileFormData formData) throws IOException
  {
    _accountService.updateMyAccountPicture(_jwtUtils.getExtIdFromToken(_jwt), formData);
    return Response.status(Status.CREATED).build();
  }

  @DELETE
  @Path("/myAccount")
  @RolesAllowed(Roles.USER)
  public Response deleteMyAccountPicture()
  {
    _accountService.deleteMyAccountPicture(_jwtUtils.getExtIdFromToken(_jwt));
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Path("/product/{id}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @RolesAllowed(Roles.ADMIN)
  public Response updateProductPicture(@PathParam("id") Long id,
    @MultipartForm @Valid FileFormData formData) throws IOException
  {
    _productService.updateProductPicture(id, formData);
    return Response.status(Status.CREATED).build();
  }

  @DELETE
  @Path("/product/{id}")
  @RolesAllowed(Roles.ADMIN)
  public Response deleteProductPicture(@PathParam("id") Long id)
  {
    _productService.deleteProductPicture(id);
    return Response.status(Status.NO_CONTENT).build();
  }
}