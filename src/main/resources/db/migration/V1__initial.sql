create table league (
    id varchar(255) PRIMARY KEY NOT NULL,
    name varchar(255) UNIQUE NOT NULL,
    draw_allowed boolean NOT NULL,
    period_length int,
    scored_results boolean NOT NULL
);

create table player (
    id varchar(255) PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL,
    rating varchar(255) NOT NULL,
    rd varchar(255) NOT NULL,
    volatility varchar(255) NOT NULL,
    league_id varchar(255) NOT NULL references League(id),
    UNIQUE (name, league_id)
);

create table game (
    id varchar(255) PRIMARY KEY NOT NULL,
    white_id varchar(255) NOT NULL references Player(id),
    black_id varchar(255) NOT NULL references Player(id),
    result int NOT NULL,
    written_result varchar(255),
    played_at TIMESTAMP NOT NULL,
    league_id varchar(255) NOT NULL references League(id)
);

create table api_user (
    username varchar(255) PRIMARY KEY NOT NULL,
    password varchar(255) NOT NULL
);

create table token (
    token varchar(255) PRIMARY KEY NOT NULL,
    username varchar(255) NOT NULL references api_user(username),
    expiry TIMESTAMP NOT NULL
);
