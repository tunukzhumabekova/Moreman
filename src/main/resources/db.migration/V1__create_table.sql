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

create table user_infos
(
    id       serial primary key,
    email    varchar unique,
    password varchar
);

insert into user_infos (email, password)
values ('string','$2a$12$qWrStX4NRsfa.kVHCRQL.eVvsYfY9F0casMvGLhxKLlfrVCCSGj/q');




