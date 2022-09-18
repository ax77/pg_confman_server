drop sequence if exists auth_privilege_idseq;
drop sequence if exists auth_resource_idseq;
drop sequence if exists auth_role_idseq;
drop sequence if exists auth_role_resource_privilege_idseq;
drop sequence if exists auth_user_idseq;
drop sequence if exists auth_user_has_roles_idseq;


-- auth_privilege
create sequence auth_privilege_idseq increment 1 owned by auth_privilege.auth_privilege_id;
alter table auth_privilege alter column auth_privilege_id set default nextval('auth_privilege_idseq');
select setval('auth_privilege_idseq', coalesce((select max(auth_privilege_id) from auth_privilege), 1));

-- auth_resource
create sequence auth_resource_idseq increment 1 owned by auth_resource.auth_resource_id;
alter table auth_resource alter column auth_resource_id set default nextval('auth_resource_idseq');
select setval('auth_resource_idseq', coalesce((select max(auth_resource_id) from auth_resource), 1));

-- auth_role
create sequence auth_role_idseq increment 1 owned by auth_role.auth_role_id;
alter table auth_role alter column auth_role_id set default nextval('auth_role_idseq');
select setval('auth_role_idseq', coalesce((select max(auth_role_id) from auth_role), 1));

-- auth_role_resource_privilege
create sequence auth_role_resource_privilege_idseq increment 1 owned by auth_role_resource_privilege.auth_role_resource_privilege_id;
alter table auth_role_resource_privilege alter column auth_role_resource_privilege_id set default nextval('auth_role_resource_privilege_idseq');
select setval('auth_role_resource_privilege_idseq', coalesce((select max(auth_role_resource_privilege_id) from auth_role_resource_privilege), 1));

-- auth_user
create sequence auth_user_idseq increment 1 owned by auth_user.auth_user_id;
alter table auth_user alter column auth_user_id set default nextval('auth_user_idseq');
select setval('auth_user_idseq', coalesce((select max(auth_user_id) from auth_user), 1));

-- auth_user_has_roles
create sequence auth_user_has_roles_idseq increment 1 owned by auth_user_has_roles.auth_user_has_roles_id;
alter table auth_user_has_roles alter column auth_user_has_roles_id set default nextval('auth_user_has_roles_idseq');
select setval('auth_user_has_roles_idseq', coalesce((select max(auth_user_has_roles_id) from auth_user_has_roles), 1));

-- let's insert something to check that all sequences are work fine
insert into auth_role(auth_role_name) values('ROLE_DUMMY_STUB');
