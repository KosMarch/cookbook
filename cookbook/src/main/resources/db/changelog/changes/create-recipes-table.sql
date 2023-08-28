--liquibase formatted sql
--changeset sql:create-recipes-table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_created DATETIME NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES recipes(id)
);

--rollback DROP TABLE recipes;
