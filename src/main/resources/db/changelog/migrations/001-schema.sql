
    create table configs (
       id varchar(255) not null,
        api_name varchar(255),
        password varchar(255),
        username varchar(255),
        tenant_id varchar(255),
        primary key (id)
    );

    create table tenants (
       id varchar(255) not null,
        active boolean not null,
        created_date timestamp not null,
        modified_date timestamp,
        name varchar(255),
        primary key (id)
    );

    create table users (
       id varchar(255) not null,
        active boolean not null,
        created_date timestamp not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        modified_date timestamp,
        password varchar(255),
        role varchar(255),
        tenant_id varchar(255),
        primary key (id)
    );

    alter table configs
       add constraint FK83ohx0f9sx088q8vm3t4reewy
       foreign key (tenant_id)
       references tenants;

    alter table users
       add constraint FK21hn1a5ja1tve7ae02fnn4cld
       foreign key (tenant_id)
       references tenants;