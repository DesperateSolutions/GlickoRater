create table rating_history (
    id SERIAL PRIMARY KEY,
    player_id varchar(255) NOT NULL references player(id),
    game_id varchar(255) references game(id),
    rating varchar(255) NOT NULL,
    rd varchar(255) NOT NULL,
    volatility varchar(255) NOT NULL,
    UNIQUE (player_id, game_id)
);