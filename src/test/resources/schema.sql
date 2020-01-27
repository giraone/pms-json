-- create data type JSONB for H2 as a JSON column
CREATE DOMAIN IF NOT EXISTS jsonb AS JSON;

CREATE TABLE company (
    id uuid not null,
    version timestamp not null,
    external_id varchar(255) not null,
    name varchar(255),
    company_address jsonb,
    tax_rel_state_code varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE employee (
    id uuid not null,
    version timestamp not null,
    company_id uuid not null,
    surname varchar(255) not null,
    given_name varchar(255),
    date_of_birth date,
    gender varchar(255),
    postal_address jsonb,
    tax_relevant_data jsonb,
    PRIMARY KEY (id)
);

ALTER TABLE company ADD CONSTRAINT constraint_company_unique_external_id UNIQUE (external_id);

ALTER TABLE employee ADD CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company;
