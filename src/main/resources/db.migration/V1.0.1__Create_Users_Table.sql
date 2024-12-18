CREATE TABLE Users
(
    id       SERIAL PRIMARY KEY,
    name     varchar(64) NOT NULL,
    email    varchar(128) UNIQUE NOT NULL,
    password varchar(128)
);