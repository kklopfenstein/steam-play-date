# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pduser (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  email                     varchar(255),
  passwd                    varchar(255),
  steam_id                  varchar(255),
  steam_long_id             varchar(255),
  constraint pk_pduser primary key (id))
;

create table play_date (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  date                      datetime,
  time                      varchar(255),
  game                      varchar(255),
  constraint pk_play_date primary key (id))
;

create table steam_friend (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  friend_steam_id           varchar(255),
  constraint pk_steam_friend primary key (id))
;

create table steam_game (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  user                      varchar(255),
  play_time                 varchar(255),
  app_id                    bigint,
  store_link                varchar(255),
  logo                      varchar(255),
  play_two_weeks            varchar(255),
  constraint pk_steam_game primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table pduser;

drop table play_date;

drop table steam_friend;

drop table steam_game;

SET FOREIGN_KEY_CHECKS=1;

