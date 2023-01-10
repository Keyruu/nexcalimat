package de.keyruu.nexcalimat.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
