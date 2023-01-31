DROP TABLE IF EXISTS "account";
CREATE TABLE "public"."account" (
    "id" uuid NOT NULL,
    "password" character varying(255),
    "role" character varying(255),
    "username" character varying(255),
    CONSTRAINT "account_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "task";
CREATE TABLE "public"."task" (
    "id" uuid NOT NULL,
    "date_time" timestamp(6),
    "description" character varying(255),
    "is_done" boolean NOT NULL,
    CONSTRAINT "task_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
