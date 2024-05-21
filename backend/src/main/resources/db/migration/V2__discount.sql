alter table if exists Account
  add column discounted boolean not null default false;

create table Discount (
  id bigserial not null,
  percentage integer not null default 50,
  primary key (id)
);

alter table if exists Discount
  add constraint valid_percentage CHECK (percentage between 0 and 100);

insert into Discount (percentage)
  values (50);