CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

CREATE TABLE resource_authority (
  id uuid PRIMARY KEY,
  target_type varchar(100) NOT NULL,
  target_id uuid NOT NULL,
  resource_type varchar(100) NOT NULL,
  resource_id varchar(100),
  permissions int4 NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by uuid NOT NULL,
  UNIQUE (target_type, target_id, resource_type, resource_id)
);

CREATE TABLE personal_information (
  id uuid PRIMARY KEY,
  first_name varchar(100),
  last_name varchar(100),
  birthdate date,
  gender bool,
  avatar_id uuid
);

CREATE TABLE tth_user (
  id uuid PRIMARY KEY,
  username varchar(100) UNIQUE NOT NULL,
  password varchar(100) NOT NULL,
  enabled bool DEFAULT FALSE NOT NULL,
  root_user bool DEFAULT FALSE NOT NULL,
  personal_information_id uuid,
  created_at timestamptz NOT NULL,
  created_by uuid NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by uuid NOT NULL,
  CONSTRAINT personal_information_fk FOREIGN KEY (personal_information_id) REFERENCES personal_information (id)
);

CREATE TABLE tth_group (
  id uuid PRIMARY KEY,
  name varchar(100) NOT NULL,
  enabled bool DEFAULT FALSE NOT NULL,
  created_at timestamptz NOT NULL,
  created_by uuid NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by uuid NOT NULL
);

CREATE TABLE group_member (
  id uuid PRIMARY KEY,
  user_id uuid NOT NULL,
  group_id uuid NOT NULL,
  modified_at timestamptz NOT NULL,
  modified_by uuid NOT NULL,
  UNIQUE (user_id, group_id),
  CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES tth_user (id),
  CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES tth_group (id)
);
