package de.keyruu.nexcalimat.graphql.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("wrong-pin")
public class WrongPinException extends RuntimeException {

}
