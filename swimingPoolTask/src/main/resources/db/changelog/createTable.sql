CREATE TABLE IF NOT EXISTS clients
(
    id    serial
        primary key,
    name  varchar(255) not null,
    phone varchar(20)  not null
        unique,
    email varchar(255) not null
        unique
);
-- auto-generated definition
CREATE TABLE IF NOT EXISTS orders
(
    id        serial
        primary key,
    client_id bigint    not null
        references clients,
    time      timestamp not null
);

-- auto-generated definition
CREATE TABLE IF NOT EXISTS time_tables
(
    time  date not null
        constraint time_table_pkey
            primary key,
    count smallint default 10
);