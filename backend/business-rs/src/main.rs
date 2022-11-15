use graphql_client::{GraphQLQuery, Response};

use crate::get_products::Variables;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let request_body = GetProducts::build_query(Variables);
    let client = reqwest::Client::new();

    let res = client
        .post("http://localhost:8080/v1/graphql")
        .header("x-hasura-admin-secret", "myadminsecretkey")
        .json(&request_body)
        .send()
        .await?;
    let response_body: Response<get_products::ResponseData> = res.json().await?;
    println!(
        "Response: {:#?}",
        response_body.data.unwrap().products[0].name
    );
    Ok(())
}

#[derive(GraphQLQuery)]
#[graphql(
    schema_path = "graphql/schema.json",
    query_path = "graphql/GetProducts.graphql",
    response_derives = "Debug"
)]
pub struct GetProducts;

type bigint = i64;
