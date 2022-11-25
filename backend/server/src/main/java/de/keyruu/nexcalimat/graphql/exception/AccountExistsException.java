package de.keyruu.nexcalimat.graphql.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("account-exists")
public class AccountExistsException extends RuntimeException {
    //
}
