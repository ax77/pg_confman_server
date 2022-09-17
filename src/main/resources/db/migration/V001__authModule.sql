create table auth_role (
    auth_role_id integer primary key,
    auth_role_name varchar(255) not null
);

create table auth_resource (
    auth_resource_id integer primary key,
    auth_resource_name varchar(255) not null
);

create table auth_privilege (
    auth_privilege_id integer primary key,
    auth_privilege_name varchar(255) not null
);

create table auth_user (
    auth_user_id integer primary key,
    auth_user_name varchar(255) not null,
    auth_user_pw varchar(255) not null,

    constraint un_auth_user_name
    unique(auth_user_name)
);

create table auth_user_has_roles (
    auth_user_has_roles_id integer primary key,
    auth_user_id integer not null,
    auth_role_id integer not null,

    constraint fk_auth_user_id
    foreign key (auth_user_id)
    references auth_user(auth_user_id)
    on update cascade
    on delete restrict,

    constraint fk_auth_role_id
    foreign key (auth_role_id)
    references auth_role(auth_role_id)
    on update cascade
    on delete restrict
);

create table auth_role_resource_privilege (
    role_resource_privilege_id integer primary key,
    auth_role_id integer not null,
    auth_resource_id integer not null,
    auth_privilege_id integer not null,

    constraint fk_auth_role_id
    foreign key (auth_role_id)
    references auth_role(auth_role_id)
    on update cascade
    on delete restrict,

    constraint fk_auth_resource_id
    foreign key (auth_resource_id)
    references auth_resource(auth_resource_id)
    on update cascade
    on delete restrict,

    constraint fk_auth_privilege_id
    foreign key (auth_privilege_id)
    references auth_privilege(auth_privilege_id)
    on update cascade
    on delete restrict
);

insert into auth_role values(101, 'ROLE_ADMIN');
insert into auth_role values(102, 'ROLE_ASSISTANT');
insert into auth_role values(103, 'ROLE_BUDGET');
insert into auth_role values(104, 'ROLE_DEPARTMENT_HEAD');
insert into auth_role values(105, 'ROLE_CEO');

insert into auth_resource values(201, 'document');
insert into auth_resource values(202, 'profession');
insert into auth_resource values(203, 'permission');

insert into auth_privilege values(301, 'INDEX');
insert into auth_privilege values(302, 'EDIT');
insert into auth_privilege values(303, 'CREATE');
insert into auth_privilege values(304, 'DELETE');
insert into auth_privilege values(305, 'SHOW');


-- the password is 'secret123'
insert into auth_user (auth_user_id, auth_user_pw, auth_user_name) values (1, '$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2', 'admin');
insert into auth_user (auth_user_id, auth_user_pw, auth_user_name) values (2, '$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2', 'assistant');
insert into auth_user (auth_user_id, auth_user_pw, auth_user_name) values (3, '$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2', 'budget');
insert into auth_user (auth_user_id, auth_user_pw, auth_user_name) values (4, '$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2', 'department_head');
insert into auth_user (auth_user_id, auth_user_pw, auth_user_name) values (5, '$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2', 'ceo');


insert into auth_user_has_roles (auth_user_has_roles_id, auth_user_id, auth_role_id) values(2000, 1, 101); -- admin
insert into auth_user_has_roles (auth_user_has_roles_id, auth_user_id, auth_role_id) values(2001, 2, 102); -- assistant
insert into auth_user_has_roles (auth_user_has_roles_id, auth_user_id, auth_role_id) values(2002, 3, 103); -- budget
insert into auth_user_has_roles (auth_user_has_roles_id, auth_user_id, auth_role_id) values(2003, 4, 104); -- department_head
insert into auth_user_has_roles (auth_user_has_roles_id, auth_user_id, auth_role_id) values(2004, 5, 105); -- ceo


-- admin-document
insert into auth_role_resource_privilege values(700, 101, 201, 301);
insert into auth_role_resource_privilege values(701, 101, 201, 302);
insert into auth_role_resource_privilege values(702, 101, 201, 303);
insert into auth_role_resource_privilege values(703, 101, 201, 304);
insert into auth_role_resource_privilege values(704, 101, 201, 305);

-- admin-profession
insert into auth_role_resource_privilege values(705, 101, 202, 301);
insert into auth_role_resource_privilege values(706, 101, 202, 302);
insert into auth_role_resource_privilege values(707, 101, 202, 303);
insert into auth_role_resource_privilege values(708, 101, 202, 304);
insert into auth_role_resource_privilege values(709, 101, 202, 305);

-- admin-permission
insert into auth_role_resource_privilege values(710, 101, 203, 301);
insert into auth_role_resource_privilege values(711, 101, 203, 302);
insert into auth_role_resource_privilege values(712, 101, 203, 303);
insert into auth_role_resource_privilege values(713, 101, 203, 304);
insert into auth_role_resource_privilege values(714, 101, 203, 305);
