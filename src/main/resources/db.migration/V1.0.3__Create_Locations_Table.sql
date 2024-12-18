CREATE TABLE Locations
(
    id        SERIAL PRIMARY KEY,
    name      varchar(128),
    user_id   int REFERENCES Users (id),
    latitude  DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL
)