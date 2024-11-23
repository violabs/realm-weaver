--uuid id PK
--string alias
--string email

CREATE TABLE player (
    id UUID PRIMARY KEY,
    alias VARCHAR(255),
    email VARCHAR(255)
);