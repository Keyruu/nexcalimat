package de.keyruu.nexcalimat.rest;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
  public Response updateAccountPicture(@PathParam("id") Long id, @MultipartForm @Valid FileFormData formData) throws IOException
  {
    _accountService.updateAccountPicture(id, formData);
    return Response.status(Status.CREATED).build();
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

  @POST
  @Path("/product/{id}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @RolesAllowed(Roles.ADMIN)
  public Response updateProductPicture(@PathParam("id") Long id, @MultipartForm @Valid FileFormData formData) throws IOException
  {
    _productService.updateProductPicture(id, formData);
    return Response.status(Status.CREATED).build();
  }
}