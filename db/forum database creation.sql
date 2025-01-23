create table tags
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint tags_pk_2
        unique (name),
    constraint check_name
        check (octet_length(`name` > 0))
);

create table users
(
    id            int auto_increment
        primary key,
    first_name    varchar(50)          not null,
    last_name     varchar(50)          not null,
    email         varchar(255)         not null,
    username      varchar(50)          not null,
    password      varchar(50)          not null,
    is_admin      tinyint(1) default 0 not null,
    profile_photo text                 null,
    phone_number  varchar(20)          null,
    constraint users_pk_2
        unique (email),
    constraint users_pk_3
        unique (username),
    constraint email
        check (`email` like '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$')
);

create table posts
(
    id          int auto_increment
        primary key,
    user_id     int           not null,
    title       varchar(100)  not null,
    content     text          not null,
    likes_count int default 0 null,
    constraint posts_pk_2
        unique (title),
    constraint posts_users_id_fk
        foreign key (user_id) references users (id)
);

create table comments
(
    id      int auto_increment
        primary key,
    post_id int  not null,
    user_id int  not null,
    content text not null,
    constraint comments_posts_id_fk
        foreign key (post_id) references posts (id)
            on delete cascade,
    constraint comments_users_id_fk
        foreign key (user_id) references users (id)
);

create table post_tags
(
    post_id int not null,
    tag_id  int not null,
    constraint post_tags_posts_id_fk
        foreign key (post_id) references posts (id)
            on delete cascade,
    constraint post_tags_tags_id_fk
        foreign key (tag_id) references tags (id)
            on delete cascade
);
