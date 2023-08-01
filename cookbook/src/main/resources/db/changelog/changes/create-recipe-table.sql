--liquibase formatted sql
--changeset sql:create-recipes-table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_created DATETIME NOT NULL,
    description VARCHAR(255) NOT NULL,
    parent_id BIGINT
);

--rollback DROP TABLE recipes;
