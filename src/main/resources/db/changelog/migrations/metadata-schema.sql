create table tenants (
                         id varchar(255) not null,
                         active boolean not null,
                         created_date timestamp not null,
                         modified_date timestamp,
                         name varchar(255),
                         primary key (id)
);