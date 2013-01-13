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

create sequence pduser_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pduser;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pduser_seq;

