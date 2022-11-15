package de.keyruu.nextcalimat.business;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import com.generated.graphql.Dragon;

import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.core.Operation;
import io.smallrye.graphql.client.core.OperationType;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.vertx.core.cli.annotations.Description;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

import java.util.List;
import java.util.concurrent.ExecutionException;

@GraphQLApi
public class BusinessResource {
  @Inject
  @GraphQLClient("hasura")
  DynamicGraphQLClient dynamicClient;

  @Query("elias")
  @Description("Purchase an article")
  public List<Dragon> purchase() throws ExecutionException, InterruptedException {
    Document query = document(
        operation(OperationType.QUERY,
            field("dragons",
                field("active"))));

    Response response = dynamicClient.executeSync(query);
    return response.getList(Dragon.class, "dragons");
  }
}