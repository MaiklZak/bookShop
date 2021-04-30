DROP TABLE IF EXISTS authors;
DROP TABLE IF EXISTS books;

CREATE TABLE authors
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(250) NOT NULL,
    second_name VARCHAR(250) NOT NULL,
    biography   TEXT DEFAULT NULL,
    link_photo  VARCHAR(250) DEFAULT NULL
);

CREATE TABLE books
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(250) NOT NULL,
    priceOld  VARCHAR(250) DEFAULT NULL,
    price     VARCHAR(250) DEFAULT NULL,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

