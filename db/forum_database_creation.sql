CREATE SCHEMA forum_management_system;

USE forum_management_system;

create table tags
(
    id   long auto_increment
        primary key,
    name varchar(50) not null,
    constraint tags_pk_2
        unique (name)
);

create table users
(
    id            long auto_increment
        primary key,
    first_name    varchar(50)          not null,
    last_name     varchar(50)          not null,
    email         varchar(255)         not null,
    username      varchar(50)          not null,
    password      varchar(255)          not null,
    is_admin      tinyint(1) default 0 not null,
    is_blocked    tinyint(1)           not null,
    profile_photo text                 null,
    phone_number  varchar(20)          null,
    constraint users_pk_2
        unique (email),
    constraint users_pk_3
        unique (username)
);

create table posts
(
    id      long auto_increment
        primary key,
    user_id long          not null,
    title   varchar(100) not null,
    content text         not null,
    constraint posts_users_id_fk
        foreign key (user_id) references users (id)
);

create table comments
(
    id      long auto_increment
        primary key,
    post_id long  not null,
    user_id long  not null,
    content text not null,
    constraint comments_posts_id_fk
        foreign key (post_id) references posts (id)
            on delete cascade,
    constraint comments_users_id_fk
        foreign key (user_id) references users (id)
);

create table post_likes_dislikes
(
    id      bigint unsigned auto_increment
        primary key,
    post_id long        not null,
    user_id long        not null,
    is_like tinyint(1) not null,
    constraint post_id
        unique (post_id, user_id),
    constraint post_likes_dislikes_ibfk_1
        foreign key (post_id) references posts (id)
            on delete cascade,
    constraint post_likes_dislikes_ibfk_2
        foreign key (user_id) references users (id)
            on delete cascade
);

create index user_id
    on post_likes_dislikes (user_id);

create table post_tags
(
    post_id long not null,
    tag_id  long not null,
    constraint post_tags_posts_id_fk
        foreign key (post_id) references posts (id)
            on delete cascade,
    constraint post_tags_tags_id_fk
        foreign key (tag_id) references tags (id)
            on delete cascade
);

