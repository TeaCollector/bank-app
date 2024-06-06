create table passport (
    passport_id  uuid,
    series       varchar(4),
    number       varchar(6),
    issue_branch varchar(255),
    issue_date   date,

    constraint pk_passport_id
        primary key (passport_id)

);

create table client (
    client_id        uuid,
    last_name        varchar(30) not null,
    first_name       varchar(30) not null,
    middle_name      varchar(30),
    birth_date       date not null,
    email            varchar not null unique,
    gender           varchar(15),
    marital_status   varchar(15),
    dependent_amount int4,
    passport_id      uuid,
    employment_id    jsonb,
    account_number   varchar(50),


    constraint pk_client_id
        primary key (client_id),
    constraint fk_client_passport_id foreign key (passport_id) references passport (passport_id)
);

create table credit (
    credit_id         uuid,
    amount            numeric not null,
    term              int4 not null,
    monthly_payment   numeric not null,
    rate              numeric,
    psk               numeric,
    payment_schedule  jsonb not null,
    insurance_enabled bool not null,
    salary_client     bool not null,
    credit_status     varchar(10),

    constraint pk_credit_id
        primary key (credit_id)
);

create table statement (
    statement_id   uuid,
    client_id      uuid,
    credit_id      uuid,
    status         varchar(30),
    creation_date  timestamptz,
    applied_offer  jsonb,
    sign_date      timestamptz,
    ses_code       int4,
    status_history jsonb,

    constraint pk_statement_id
        primary key (statement_id)
);