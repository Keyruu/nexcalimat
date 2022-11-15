CREATE TABLE "public"."products" ("id" serial NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), "updated_at" timestamptz NOT NULL DEFAULT now(), "deleted_at" timestamptz NOT NULL, "name" text NOT NULL, "picture" text, "price" bigint NOT NULL, "bundle_size" integer NOT NULL, "type" Text NOT NULL, PRIMARY KEY ("id") );
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
CREATE TRIGGER "set_public_products_updated_at"
BEFORE UPDATE ON "public"."products"
FOR EACH ROW
EXECUTE PROCEDURE "public"."set_current_timestamp_updated_at"();
COMMENT ON TRIGGER "set_public_products_updated_at" ON "public"."products" 
IS 'trigger to set value of column "updated_at" to current timestamp on row update';
