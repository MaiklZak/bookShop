alter table book_genre drop if exists fk_book_genre_book_id;
alter table book_genre drop if exists fk_book_genre_genre_id;
alter table books drop if exists fk_books_author_id;
alter table user_book drop if exists fk_user_book_book_id;
alter table user_book drop if exists fk_user_book_user_id;

drop table if exists authors cascade;
drop table if exists book_genre cascade;
drop table if exists books cascade;
drop table if exists documents cascade;
drop table if exists genres cascade;
drop table if exists test_entities cascade;
drop table if exists user_book cascade;
drop table if exists users cascade;
create table authors
(
    id         serial not null,
    first_name varchar(255),
    last_name  varchar(255),
    primary key (id)
);
create table book_genre
(
    genre_id int4 not null,
    book_id  int4 not null,
    primary key (genre_id, book_id)
);
create table books
(
    id        serial not null,
    price     varchar(255),
    price_old varchar(255),
    title     varchar(255),
    author_id int4,
    primary key (id)
);
create table documents
(
    id    serial not null,
    text  varchar(255),
    title varchar(255),
    primary key (id)
);
create table genres
(
    id   serial not null,
    name varchar(255),
    primary key (id)
);
create table test_entities
(
    id   bigserial not null,
    data varchar(255),
    primary key (id)
);
create table user_book
(
    user_id int4 not null,
    book_id int4 not null,
    primary key (user_id, book_id)
);
create table users
(
    id    serial not null,
    email varchar(255),
    name  varchar(255),
    primary key (id)
);
alter table book_genre
    add constraint fk_book_genre_book_id foreign key (book_id) references books;
alter table book_genre
    add constraint fk_book_genre_genre_id foreign key (genre_id) references genres;
alter table books
    add constraint fk_books_author_id foreign key (author_id) references authors;
alter table user_book
    add constraint fk_user_book_book_id foreign key (book_id) references books;
alter table user_book
    add constraint fk_user_book_user_id foreign key (user_id) references users;