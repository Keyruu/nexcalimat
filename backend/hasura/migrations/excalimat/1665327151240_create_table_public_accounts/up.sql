CREATE TABLE "public"."accounts" ("id" serial NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), "updated_at" timestamptz NOT NULL DEFAULT now(), "deleted_at" timestamptz NOT NULL, "ext_id" Text NOT NULL, "email" text NOT NULL, "name" text NOT NULL, "balance" bigint NOT NULL, "picture" text NOT NULL, "pin" text NOT NULL, PRIMARY KEY ("id") );
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
CREATE TRIGGER "set_public_accounts_updated_at"
BEFORE UPDATE ON "public"."accounts"
FOR EACH ROW
EXECUTE PROCEDURE "public"."set_current_timestamp_updated_at"();
COMMENT ON TRIGGER "set_public_accounts_updated_at" ON "public"."accounts" 
IS 'trigger to set value of column "updated_at" to current timestamp on row update';
