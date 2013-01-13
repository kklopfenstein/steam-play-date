# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pduser (
  name                      varchar(255) not null,
  email                     varchar(255),
  passwd                    varchar(255),
  steam_id                  varchar(255),
  steam_long_id             varchar(255),
  constraint pk_pduser primary key (name))
;

create table play_date (
  user                      varchar(255) not null,
  date                      timestamp not null,
  time                      varchar(255) not null,
  game                      varchar(255))
;

create table steam_game (
  name                      varchar(255) not null,
  user                      varchar(255) not null,
  play_time                 varchar(255),
  app_id                    bigint,
  store_link                varchar(255),
  logo                      varchar(255),
  play_two_weeks            varchar(255))
;

create sequence pduser_seq;

create sequence play_date_seq;

create sequence steam_game_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pduser;

drop table if exists play_date;

drop table if exists steam_game;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pduser_seq;

drop sequence if exists play_date_seq;

drop sequence if exists steam_game_seq;

