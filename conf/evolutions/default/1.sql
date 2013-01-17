# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pduser (
  id                        bigint not null,
  name                      varchar(255),
  email                     varchar(255),
  passwd                    varchar(255),
  steam_id                  varchar(255),
  steam_long_id             varchar(255),
  constraint pk_pduser primary key (id))
;

create table play_date (
  id                        bigint not null,
  user                      varchar(255),
  date                      timestamp,
  time                      varchar(255),
  game                      varchar(255),
  constraint pk_play_date primary key (id))
;

create table steam_friend (
  id                        bigint not null,
  user                      varchar(255),
  friend_steam_id           varchar(255),
  constraint pk_steam_friend primary key (id))
;

create table steam_game (
  id                        bigint not null,
  name                      varchar(255),
  user                      varchar(255),
  play_time                 varchar(255),
  app_id                    bigint,
  store_link                varchar(255),
  logo                      varchar(255),
  play_two_weeks            varchar(255),
  constraint pk_steam_game primary key (id))
;

create sequence pduser_seq;

create sequence play_date_seq;

create sequence steam_friend_seq;

create sequence steam_game_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pduser;

drop table if exists play_date;

drop table if exists steam_friend;

drop table if exists steam_game;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pduser_seq;

drop sequence if exists play_date_seq;

drop sequence if exists steam_friend_seq;

drop sequence if exists steam_game_seq;

