package de.keyruu.nexcalimat.graphql.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("account-not-found")
public class AccountNotFoundException extends RuntimeException {

}
