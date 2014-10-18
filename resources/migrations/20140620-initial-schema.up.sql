create table users (
  id serial primary key,
  sid text unique,
  name text unique
);

create table channels (
  id serial primary key,
  sid text unique,
  name text unique,
  date timestamp
);

create table messages (
  id serial primary key,
  channel_id int references channels (id),
  user_id int references users(id),
  type text,
  subtype text,
  hidden boolean default 'true',
  date timestamp,
  text text
);
