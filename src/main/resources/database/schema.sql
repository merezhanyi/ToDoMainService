DROP TABLE IF EXISTS "account";
DROP SEQUENCE IF EXISTS account_id_seq;
CREATE SEQUENCE account_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."account" (
    "id" bigint DEFAULT nextval('account_id_seq') NOT NULL,
    "password" character varying(255),
    "role" character varying(255),
    "username" character varying(255),
    CONSTRAINT "account_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "task";
DROP SEQUENCE IF EXISTS task_id_seq;
CREATE SEQUENCE task_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."task" (
    "id" bigint DEFAULT nextval('task_id_seq') NOT NULL,
    "date_time" timestamp(6),
    "description" character varying(255),
    "is_done" boolean NOT NULL,
    CONSTRAINT "task_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
