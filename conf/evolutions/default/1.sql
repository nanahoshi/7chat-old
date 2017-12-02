# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table context (
  previous_keyword              varchar(255),
  previous_keyword_type         varchar(255),
  current_keyword               varchar(255),
  current_keyword_type          varchar(255),
  after_keyword                 varchar(255),
  after_keyword_type            varchar(255),
  frequency                     integer,
  is_end                        boolean,
  is_able_to_begin              boolean,
  total_length                  integer
);

create table conversations (
  channel                       varchar(255),
  user_id                       varchar(255),
  conversation_id               varchar(255),
  content                       varchar(255),
  reply                         varchar(255)
);


# --- !Downs

drop table if exists context cascade;

drop table if exists conversations cascade;

