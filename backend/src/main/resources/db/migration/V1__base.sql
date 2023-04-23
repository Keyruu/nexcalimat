create table Account (
    id bigserial not null,
    balance bigint not null,
    created_at timestamp(6),
    deleted_at timestamp(6),
    email varchar(255) not null,
    ext_id varchar(255) not null unique,
    name varchar(255) not null,
    picture varchar(3000),
    pin_hash varchar(255),
    primary key (id)
);

create table Favorite (
    id bigserial not null,
    account_id bigint,
    product_id bigint,
    primary key (id),
    constraint favorite_unique unique (account_id, product_id)
);

create table Product (
    id bigserial not null,
    bundle_size integer not null,
    created_at timestamp(6),
    deleted_at timestamp(6),
    name varchar(255) not null,
    picture varchar(3000),
    price integer not null,
    type smallint not null check (type between 0 and 1),
    primary key (id)
);

create view product_with_favorite as
  select p.*,
    case when f.id is null then false else true end as is_favorite, f.account_id 
    from product p left join favorite f on p.id = f.product_id;

create table Purchase (
    id bigserial not null,
    created_at timestamp(6),
    deleted_at timestamp(6),
    paid_price integer not null,
    account_id bigint,
    product_id bigint,
    primary key (id)
);

alter table if exists Favorite 
  add constraint favorite_account_fk 
  foreign key (account_id) 
  references Account;

alter table if exists Favorite 
  add constraint favorite_product_fk 
  foreign key (product_id) 
  references Product;

alter table if exists Purchase 
  add constraint purchase_account_fk 
  foreign key (account_id) 
  references Account;

alter table if exists Purchase 
  add constraint purchase_product_fk 
  foreign key (product_id) 
  references Product;
