drop table if exists pc_manufacturer cascade;
drop table if exists pc_proc cascade;
drop table if exists pc_socket cascade;
drop table if exists pc_mboard cascade;
drop table if exists pc_storage cascade;
drop table if exists pc_ram cascade;
drop table if exists pc_cooler cascade;
drop table if exists pc_cooler_has_socket cascade;
drop table if exists pc_power_supply cascade;
drop table if exists pc_case_type cascade;
drop table if exists pc_case cascade;
drop table if exists pc_case_has_type cascade;

create table pc_manufacturer (
    pc_manufacturer_id integer primary key,
    pc_manufacturer_name varchar(255) not null,
    
    constraint un_pc_manufacturer_name 
    unique(pc_manufacturer_name)
);

create table pc_socket (
    pc_socket_id integer primary key,
    pc_socket_name varchar(255) not null,
    
    constraint un_pc_socket_vendor 
    unique(pc_socket_id, pc_socket_name)
);

create table pc_proc (
    pc_proc_id integer primary key,
    pc_proc_name varchar(255) not null, 
    
    pc_socket_id integer not null,
    constraint fk_pc_socket_id 
    foreign key(pc_socket_id)
    references pc_socket(pc_socket_id)
    on update cascade
    on delete restrict,
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict,
    
    pc_proc_core_series varchar(255) not null, -- intel core i7, amd ryzen 5
    pc_proc_microarch varchar(255) not null, -- comet lake, alder lake, zen 3
    pc_proc_launch_date timestamp not null,
    pc_proc_frequency integer not null,
    pc_proc_cores integer not null,
    pc_proc_threads integer not null
    
);

create table pc_mboard (
    pc_mboard_id integer primary key,
    pc_mboard_name varchar(255) not null,
    
    pc_socket_id integer not null,
    constraint fk_pc_socket_id 
    foreign key(pc_socket_id)
    references pc_socket(pc_socket_id)
    on update cascade
    on delete restrict,  

    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict    
);

create table pc_storage (
    pc_storage_id integer primary key,
    pc_storage_name varchar(255),
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict,

    pc_storage_capacity integer not null
);

create table pc_ram (
    pc_ram_id integer primary key,
    pc_ram_name varchar(255) not null,
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict,

    pc_ram_frequency integer not null,
    pc_ram_capacity integer not null
);

create table pc_cooler (
    pc_cooler_id integer primary key,
    pc_cooler_name varchar(255) not null,
    
    pc_socket_id integer not null,
    constraint fk_pc_socket_id 
    foreign key(pc_socket_id)
    references pc_socket(pc_socket_id)
    on update cascade
    on delete restrict,
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict,
    
    -- water cooling system
    pc_cooler_wc boolean not null default false
);

create table pc_cooler_has_socket (
    pc_cooler_has_socket_id integer primary key,
    
    pc_cooler_id integer not null,
    constraint fk_pc_cooler_id
    foreign key(pc_cooler_id)
    references pc_cooler(pc_cooler_id)
    on update cascade
    on delete restrict, 

    pc_socket_id integer not null,
    constraint fk_pc_socket_id
    foreign key(pc_socket_id)
    references pc_socket(pc_socket_id)
    on update cascade
    on delete restrict    
);

create table pc_power_supply (
    pc_power_supply_id integer primary key,
    pc_power_supply_name varchar(255) not null,
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict,
    
    pc_power_supply_wattage integer not null,
    pc_cooler_modular boolean not null default false
);

create table pc_case_type (
    pc_case_type_id integer primary key,
    pc_case_type_name varchar(255) not null
);

create table pc_case (
    pc_case_id integer primary key,
    pc_case_name varchar(255) not null,
    
    pc_manufacturer_id integer not null,
    constraint fk_pc_manufacturer_id 
    foreign key(pc_manufacturer_id)
    references pc_manufacturer(pc_manufacturer_id)
    on update cascade
    on delete restrict
);

create table pc_case_has_type (
    pc_case_has_type_id integer primary key,
    
    pc_case_id integer not null,
    constraint fk_pc_case_id
    foreign key(pc_case_id)
    references pc_case(pc_case_id)
    on update cascade
    on delete restrict,
    
    pc_case_type_id integer not null,
    constraint fk_pc_case_type_id
    foreign key(pc_case_type_id)
    references pc_case_type(pc_case_type_id)
    on update cascade
    on delete restrict    
);



insert into pc_case_type values (100, 'ATX Desktop');
insert into pc_case_type values (101, 'ATX Full Tower');
insert into pc_case_type values (102, 'ATX Mid Tower');
insert into pc_case_type values (103, 'ATX Mini Tower');
insert into pc_case_type values (104, 'MicroATX Desktop');
insert into pc_case_type values (105, 'MicroATX Mid Tower');
insert into pc_case_type values (106, 'MicroATX Mini Tower');
insert into pc_case_type values (107, 'Mini ITX Desktop');
insert into pc_case_type values (108, 'Mini ITX Tower');

