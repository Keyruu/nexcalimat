import psycopg2
import requests
from psycopg2.extras import execute_values

# Connect to your databases
old_conn = psycopg2.connect(
    "dbname=excalimat user=test password=postgres host=localhost port=7777"
)
new_conn = psycopg2.connect(
    "dbname=quarkus user=quarkus password=quarkus host=localhost port=56517"
)

headers = {"Authorization": "Bearer "}

userMap = {}
productMap = {}

try:
    old_cursor = old_conn.cursor()
    new_cursor = new_conn.cursor()

    # Migrate accounts
    old_cursor.execute("SELECT * FROM account")
    for row in old_cursor:
        identifier, firstname, surname, balance, _, _, _, _, _ = row

        name = f"{firstname} {surname}"

        # Find user in Azure AD with a matching display name with get request add authorization header
        users = requests.get(
            f"https://graph.microsoft.com/v1.0/users?$filter=displayName eq '{name}'",
            headers=headers,
        ).json()["value"]

        user = None

        if len(users) == 0:
            print(f"User '{name}' not found in Azure AD, please provide the email:")
            email = input()

            if not email:
                print("No email provided, skipping account")
                continue

            # Find user in Azure AD with a matching email with get request add authorization header
            users = requests.get(
                f"https://graph.microsoft.com/v1.0/users?$filter=mail eq '{email}'",
                headers=headers,
            ).json()["value"]

            if len(users) == 0:
                print(f"User '{email}' not found in Azure AD, skipping account")
                continue
            else:
                user = users[0]
        else:
            user = users[0]

        new_cursor.execute(
            "INSERT INTO Account (created_at, balance, ext_id, name, email) VALUES (now(), %s, %s, %s, %s) RETURNING id",
            (balance, user["id"], name, user["mail"]),
        )

        userMap[identifier] = new_cursor.fetchone()[0]

    # Migrate products
    old_cursor.execute("SELECT * FROM product")
    for row in old_cursor:
        id, name, _, price, product_type, _, _, _, archive, _, _, _, _, _, _ = row
        if product_type == "milk":
            print(f"Skipping product '{name}' because it is of type 'MILK'")
            continue
        elif product_type == "hotdrink":
            type = 0
            bundle_size = 0
        else:
            type = 1
            print(f"Provide the bundle size for product '{name}':")
            bundle_size = input()
            if not bundle_size:
                bundle_size = 0

        deleted_at = None

        if archive:
            deleted_at = "now()"

        new_cursor.execute(
            "INSERT INTO Product (created_at, deleted_at, name, price, type, bundle_size) VALUES (now(), %s, %s, %s, %s, %s) RETURNING id",
            (deleted_at, name, price, type, bundle_size),
        )

        productMap[id] = new_cursor.fetchone()[0]

    # Migrate purchases
    old_cursor.execute("SELECT * FROM purchase")
    rows = old_cursor.fetchall()
    for row in rows:
        id, account, product, timestamp, paid_price, _ = row

        isRefundend = False

        old_cursor.execute("SELECT * FROM refund WHERE purchase = %s", (id,))
        if old_cursor.rowcount > 0:
            isRefundend = True

        account_id = userMap.get(account)
        product_id = productMap.get(product)

        if not account_id:
            print(f"Account '{account}' not found, skipping purchase")
            continue

        if not product_id:
            print(f"Product '{product}' not found, skipping purchase")
            continue

        # Convert old account identifier to new account id
        new_cursor.execute(
            "INSERT INTO Purchase (created_at, deleted_at, paid_price, account_id, product_id) VALUES (%s, %s, %s, %s, %s)",
            (
                timestamp,
                "now()" if isRefundend else None,
                paid_price,
                account_id,
                product_id,
            ),
        )

    # Commit the changes
    new_conn.commit()

finally:
    # Close the connections
    old_cursor.close()
    old_conn.close()
    new_cursor.close()
    new_conn.close()
