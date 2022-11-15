CREATE TABLE "public"."purchases" ("id" bigserial NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), "updated_at" timestamptz NOT NULL DEFAULT now(), "deleted_at" timestamptz NOT NULL, "account_id" integer NOT NULL, "product_id" integer NOT NULL, "paid_price" bigint NOT NULL, PRIMARY KEY ("id") , FOREIGN KEY ("account_id") REFERENCES "public"."accounts"("id") ON UPDATE restrict ON DELETE cascade, FOREIGN KEY ("product_id") REFERENCES "public"."products"("id") ON UPDATE restrict ON DELETE cascade);
CREATE OR REPLACE FUNCTION "public"."set_current_timestamp_updated_at"()
RETURNS TRIGGER AS $$
DECLARE
  _new record;
BEGIN
  _new := NEW;
  _new."updated_at" = NOW();
  RETURN _new;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER "set_public_purchases_updated_at"
BEFORE UPDATE ON "public"."purchases"
FOR EACH ROW
EXECUTE PROCEDURE "public"."set_current_timestamp_updated_at"();
COMMENT ON TRIGGER "set_public_purchases_updated_at" ON "public"."purchases" 
IS 'trigger to set value of column "updated_at" to current timestamp on row update';
