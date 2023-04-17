package de.keyruu.nexcalimat.rest.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import de.keyruu.nexcalimat.filestore.exception.WrongFileTypeException;

@Provider
public class WrongFileTypeExceptionMapper implements ExceptionMapper<WrongFileTypeException>
{
  @Override
  public Response toResponse(WrongFileTypeException exception)
  {
    return Response.status(Status.BAD_REQUEST).entity("Wrong file type!").build();
  }
}
