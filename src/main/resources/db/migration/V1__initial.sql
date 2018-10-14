create table league (
    id varchar(255) PRIMARY KEY,
    name varchar(255) UNIQUE,
    draw_allowed boolean,
    period_length int,
    scored_results boolean
);

create table player (
    id varchar(255) PRIMARY KEY,
    name varchar(255),
    rating varchar(255),
    rd varchar(255),
    volatility varchar(255),
    league_id varchar(255) references League(id),
    UNIQUE (name, league_id)
);

create table game (
    id varchar(255) PRIMARY KEY,
    white_id varchar(255) references Player(id),
    black_id varchar(255) references Player(id),
    result int,
    written_result varchar(255),
    played_at TIMESTAMP,
    league_id varchar(255) references League(id)
);

create table api_user (
    username varchar(255) PRIMARY KEY,
    password varchar(255)
);

create table token (
    token varchar(255) PRIMARY KEY,
    username varchar(255) references api_user(username),
    expiry TIMESTAMP
);
