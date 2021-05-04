DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE books
(
    id       BIGSERIAL PRIMARY KEY,
    author   VARCHAR(250) NOT NULL,
    title    VARCHAR(250) NOT NULL,
    priceOld VARCHAR(250) DEFAULT NULL,
    price    VARCHAR(250) DEFAULT NULL
);

create table authors
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name  VARCHAR(50)
);

