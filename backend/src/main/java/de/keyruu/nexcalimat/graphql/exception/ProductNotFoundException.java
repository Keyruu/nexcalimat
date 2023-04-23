package de.keyruu.nexcalimat.graphql.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("product-not-found")
public class ProductNotFoundException extends RuntimeException
{
    //
}
