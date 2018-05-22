insert into T_PARAMETER (name, PARAMETER_TYPE, DESCRIPTION , RANK ) values ('medicalEntityID', 'TEXT', 'ИД ЛПУ', 1);
insert into T_PARAMETER (name, PARAMETER_TYPE, DESCRIPTION , RANK ) values ('roleID', 'TEXT', 'ИД роли пользователя', 2);
insert into T_PARAMETER (name, PARAMETER_TYPE, DESCRIPTION , RANK ) values ('newUI', 'BOOLEAN', 'NewUI', 3);

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ врача', '/web/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '1');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ заведующего отделением', '/web-department-manager/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '27');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ специалиста по ЗК', '/web-specialist-llo-app/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '101');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ специалиста УФ ДЗМ', '/web-specialist-llo-app/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '100');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ администратора ЛЛО', '/web-admin-llo-app/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '21');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ регистратора ЛЛО', '/web-registrator-llo-app/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '<ИД роли регистратора ЛЛО>');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ Регистратора', '/web-registrator/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '5');
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '20');
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '7');
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '41');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');

insert into T_RULE(NAME, TARGET_URL) values ('переход в АРМ Ответственного', '/web-attacher/accessPoint/index.api');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'roleID'), (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), '40');
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (true, 'EQ', (select id from T_PARAMETER where name = 'medicalEntityID'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE(ANY, OPERATION_TYPE, PARAMETER_ID, RULE_ID) values (false, 'EQ', (select id from T_PARAMETER where name = 'newUI'),  (select max(id) from T_RULE) );
insert into T_RULE_PARAMETER_VALUE_LIST (RULE_PARAMETER_VALUE_ID, VALUES) values ((select max(id) from T_RULE_PARAMETER_VALUE), 'true');
