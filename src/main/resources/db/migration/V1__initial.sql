create table League (
    id varchar(255) PRIMARY KEY,
    name varchar(255),
    draw_allowed boolean,
    period_length int,
    scored_results boolean
);

create table Player (
    id varchar(255) PRIMARY KEY,
    name varchar(255),
    rating varchar(255),
    rd varchar(255),
    volatility varchar(255),
    league_id varchar(255) references League(id),
    UNIQUE (name, league_id)
);

create table Game (
    id varchar(255) PRIMARY KEY,
    white_id varchar(255) references Player(id),
    black_id varchar(255) references Player(id),
    result int,
    written_result varchar(255),
    played_at TIMESTAMP
);

create table api_user (
    username varchar(255) PRIMARY KEY,
    password varchar(255)
);

create table Token (
    token varchar(255) PRIMARY KEY,
    username varchar(255) references api_user(username),
    expiry timestamp
);
