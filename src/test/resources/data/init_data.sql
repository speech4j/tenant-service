create schema if not exists metadata;
create schema if not exists test_tenant_1;


CREATE TABLE metadata.tenants
(
    id VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    createddate date NOT NULL,
    description VARCHAR(255),
    modifieddate date NOT NULL,
    CONSTRAINT "tenantsPK" PRIMARY KEY (id)
);
CREATE TABLE test_tenant_1.tenant_configs (
    id VARCHAR(255) NOT NULL,
    apiname VARCHAR(255),
    credentials TEXT,
    tenantid VARCHAR(255),
    CONSTRAINT "tenant_configsPK"
    PRIMARY KEY (id)
);
CREATE TABLE test_tenant_1.tenant_users (
    id VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    createddate date NOT NULL,
    email VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    modifieddate date,
    password VARCHAR(255),
    role VARCHAR(255),
    tenantid VARCHAR(255),
    CONSTRAINT "tenant_usersPK" PRIMARY KEY (id)
);

INSERT INTO metadata.tenants VALUES
('test_tenant_1', true, '2020-6-06'::timestamp without time zone, 'Company-1', '2020-6-04'::timestamp without time zone),
('test_tenant_2', true, '2020-6-04'::timestamp without time zone, 'Company-2', '2020-6-04'::timestamp without time zone),
('test_tenant_3', true, '2020-6-04'::timestamp without time zone, 'Company-3', '2020-6-04'::timestamp without time zone);


INSERT INTO test_tenant_1.tenant_configs VALUES
('1', 'GOOGLE', '{"username":"mslob","password":"qwerty123"}', 'test_tenant_1'),
('2', 'AWS', '{"username":"mslob","password":"qwerty123"}', 'test_tenant_1');

INSERT INTO test_tenant_1.tenant_users VALUES
('1',true, '2020-6-04'::timestamp without time zone, 'email1@gmail.com', 'Name1',
 'Surname1', '2020-6-04'::timestamp without time zone, 'Qwerty123','ADMIN', 'test_tenant_1'),
('2',true, '2020-6-04'::timestamp without time zone, 'email2@gmail.com', 'Name1',
 'Surname2', '2020-6-04'::timestamp without time zone, 'Qwerty123', 'ADMIN','test_tenant_1')

