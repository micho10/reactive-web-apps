--  Statements to run when updating to this version of the schema
# --- !Ups
CREATE TABLE "user" (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  firstname VARCHAR NOT NULL,
  lastname VARCHAR NOT NULL
);

--  Statements to run when downgrading from this version of the schema
# --- !Downs
DROP TABLE "user";
