

create table category (
                          id serial primary key,
                          image varchar,
                          name varchar
);

create table products(
    id serial primary key,
    image varchar,
    name varchar,
    price integer,
    quantity integer,
    description varchar,
    popular boolean,
    category_id integer references category (id)

);


create table orders(
    id serial primary key,
    product_id integer references products(id),
    quantity integer,
    date timestamp,
        CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE

);


ALTER TABLE products
    ADD CONSTRAINT fk_user_quids
        FOREIGN KEY (category_id)
            REFERENCES category(id);



create type role as enum ('ADMIN', 'USER');


create table user_infos
(
    id       serial primary key,
    email    varchar unique,
    password varchar,
    role     role
);

create table users
(
    id           serial primary key,
    user_info_id integer references user_infos (id)

);
