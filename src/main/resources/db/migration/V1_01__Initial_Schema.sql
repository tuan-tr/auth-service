CREATE TABLE resource_authority (
  id varchar PRIMARY KEY,
  target_type varchar NOT NULL,
  target_id varchar NOT NULL,
  resource_type varchar NOT NULL,
  resource_id varchar,
  permissions int4 NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by varchar NOT NULL,
  UNIQUE (target_type, target_id, resource_type, resource_id)
);

CREATE TABLE personal_information (
  id varchar PRIMARY KEY,
  first_name varchar(100),
  last_name varchar(100),
  birthdate date,
  gender bool,
  avatar_id varchar
);

CREATE TABLE _user (
  id varchar PRIMARY KEY,
  username varchar UNIQUE NOT NULL,
  password varchar NOT NULL,
  enabled bool DEFAULT FALSE NOT NULL,
  root_user bool DEFAULT FALSE NOT NULL,
  personal_information_id varchar,
  created_at timestamptz NOT NULL,
  created_by varchar NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by varchar NOT NULL,
  CONSTRAINT personal_information_fk FOREIGN KEY (personal_information_id) REFERENCES personal_information (id)
);

CREATE TABLE _group (
  id varchar PRIMARY KEY,
  name varchar NOT NULL,
  enabled bool DEFAULT FALSE NOT NULL,
  created_at timestamptz NOT NULL,
  created_by varchar NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by varchar NOT NULL
);

CREATE TABLE group_member (
  id varchar PRIMARY KEY,
  user_id varchar NOT NULL,
  group_id varchar NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by varchar NOT NULL,
  UNIQUE (user_id, group_id),
  CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES _user (id),
  CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES _group (id)
);
