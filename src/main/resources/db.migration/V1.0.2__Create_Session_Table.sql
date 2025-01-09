CREATE TABLE Sessions
(
    id uuid PRIMARY KEY DEFAULT public.uuid_generate_v4(),
    user_id   int       NOT NULL REFERENCES Users (id),
    expire_at timestamp NOT NULL
);