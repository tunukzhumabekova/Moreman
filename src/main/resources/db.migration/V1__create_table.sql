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

insert into category (image, name)
values ('1734161749465Category (1).png','сеты'),
       ('1734161705274Category (3).png','суши'),
       ('1734098385740Category.png','супы'),
       ('1734161798490Icon.png','десерты'),
       ('1734161824064Category (4).png','закуски'),
       ('1734161855713Category (5).png','поке боулы'),
       ('1734161882313Category (6).png','салаты'),
       ('1734161903401Category (7).png','горячее'),
       ('1734161924973Category (8).png','сашими'),
       ('1734161949218Icon (1).png','напитки')


