DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS card CASCADE;
DROP TABLE IF EXISTS card_session CASCADE;
DROP TABLE IF EXISTS transaction_log CASCADE;
DROP TABLE IF EXISTS card_login_unsuccessful_attempt CASCADE;


CREATE TABLE client
(
    id         INT GENERATED ALWAYS AS IDENTITY UNIQUE PRIMARY KEY,
    first_name TEXT,
    last_name  TEXT
);

CREATE TABLE account
(
    id        INT GENERATED ALWAYS AS IDENTITY UNIQUE PRIMARY KEY,
    amount    DECIMAL                    NOT NULL,
    client_id INT REFERENCES client (id) NOT NULL,
    CONSTRAINT amount CHECK (amount >= 0)
);

CREATE TABLE card
(
    id                INT GENERATED ALWAYS AS IDENTITY UNIQUE PRIMARY KEY,
    number            NUMERIC(16) UNIQUE          NOT NULL,
    account_id        INT REFERENCES account (id) NOT NULL,
    password          TEXT                        NOT NULL,
    blocked_timestamp TIMESTAMP DEFAULT NULL,
    issued_until      TIMESTAMP                   NOT NULL,
    login_method      TEXT
);
CREATE TABLE card_login_unsuccessful_attempt
(
    id           INT GENERATED ALWAYS AS IDENTITY UNIQUE PRIMARY KEY,
    card_number  NUMERIC(16) NOT NULL REFERENCES card (number),
    attempt_time TIMESTAMP   NOT NULL
);


CREATE TABLE card_session
(
    id          INT GENERATED ALWAYS AS IDENTITY UNIQUE PRIMARY KEY,
    valid_until TIMESTAMP   NOT NULL,
    token       TEXT        NOT NULL,
    card_number NUMERIC(16) NOT NULL REFERENCES card (number)
);



INSERT INTO client (id, first_name, last_name) OVERRIDING SYSTEM VALUE
VALUES (1, 'Roman', 'Oborin'),
       (2, 'Santyago', 'Romiros'),
       (3, 'Bryan', 'Basham');

INSERT INTO account (id, amount, client_id) OVERRIDING SYSTEM VALUE
VALUES (1, 1000, 1),
       (2, 5000, 2),
       (3, 10000, 3);

INSERT INTO card (id, number, account_id, password, issued_until) OVERRIDING SYSTEM VALUE
VALUES (1, 4200550066007131, 1, '1234', '2027-01-01'),
       (2, 4200550066007232, 2, '2222', '2027-01-01'),
       (3, 4200550066007333, 3, '6543', '2027-01-01');
