-- Inserting into meta_permission
INSERT INTO meta_permission (id,version,created_on,active,name,object,created_by) 
VALUES (nextval('meta_permission_seq'),0,(SELECT NOW()::timestamp),true,'perm.contact.isSubjectToTaxes.re', 'com.axelor.contact.db.Contact',1);



-- Inserting into meta_permission_rule
INSERT INTO meta_permission_rule (id,version,created_on,can_read,can_export,can_write,field,created_by,meta_permission) 
VALUES (nextval('meta_permission_rule_seq'),0,(SELECT NOW()::timestamp), 'true', 'true', 'false', 'isSubjectToTaxes',1, currval('meta_permission_seq'));




-- Inserting into auth_user
INSERT INTO auth_user (id,version,created_on,blocked,code,email,force_password_change,name,no_help,password,single_tab,created_by) 
VALUES (nextval('auth_user_seq'),1,(SELECT NOW()::timestamp),false,'hruser','hr@gmail.com',false,'Humane Resoorce',false,'hruser',false,1);



-- Inserting into auth_role
INSERT INTO auth_role (id,version,created_on,description,name,created_by)
VALUES (nextval('auth_role_seq'),0,(SELECT NOW()::timestamp),'User managing contacts','CRM',1);


-- Inserting into auth_user_roles
INSERT INTO auth_user_roles (auth_user, roles)
VALUES (currval('auth_user_seq'), (SELECT id FROM auth_role WHERE name = 'CRM'));



-- Inserting into auth_permission for Contact

INSERT INTO auth_permission(id,version,created_on,can_create,can_read,can_export,can_remove,can_write,name,object,condition_value,condition_params,created_by) 
VALUES (nextval('auth_permission_seq'),0,(SELECT NOW()::timestamp),'true','true','true','true','true','perm.contact.rwcde','com.axelor.contact.db.Contact','','',1);



-- Inserting into auth_role_permissions for Contact
INSERT INTO auth_role_permissions (auth_role, permissions)
VALUES ((SELECT id FROM auth_role WHERE name = 'CRM'), currval('auth_permission_seq'));



-- Inserting into auth_permission for Address
INSERT INTO auth_permission(id,version,created_on,can_create,can_read,can_export,can_remove,can_write,name,object,condition_value,condition_params,created_by)
VALUES(nextval('auth_permission_seq'),0,(SELECT NOW()::timestamp),'true','true','true','true','true','perm.address.rwcde','com.axelor.contact.db.Address','','',1);


-- Inserting into auth_role_permissions for Address
INSERT INTO auth_role_permissions (auth_role, permissions)
VALUES ((SELECT id FROM auth_role WHERE name = 'CRM'), currval('auth_permission_seq'));



-- Inserting into auth_permission for User
INSERT INTO auth_permission(id,version,created_on,can_create,can_read,can_export,can_remove,can_write,name,object,condition_value,condition_params,created_by) 
VALUES (nextval('auth_permission_seq'),0,(SELECT NOW()::timestamp),'false','true','true','false','false','perm.sale.re','com.axelor.sale.db.SaleOrderLine','','',1);

-- Inserting into auth_role_permissions for User
INSERT INTO auth_role_permissions (auth_role, permissions)
VALUES ((SELECT id FROM auth_role WHERE name = 'CRM'), currval('auth_permission_seq'));


-- Inserting into auth_permission for SaleOrderLine
INSERT INTO auth_permission(id,version,created_on,can_create,can_read,can_export,can_remove,can_write,name,object,condition_value,condition_params,created_by) 
VALUES  (nextval('auth_permission_seq'),0,(SELECT NOW()::timestamp),'false','true','false','false','true','perm.user.self.rw','com.axelor.auth.db.User','self.id = ?','__user__.id',1);

-- Inserting into auth_role_permissions for SaleOrderLine
INSERT INTO auth_role_permissions (auth_role, permissions)
VALUES ((SELECT id FROM auth_role WHERE name = 'CRM'), currval('auth_permission_seq'));



-- Inserting into meta_menu_roles
INSERT INTO meta_menu_roles (menus, roles)
VALUES ((SELECT id FROM meta_menu WHERE name = 'contact-root-configuration-addresses'), (select currval('auth_role_seq')));

INSERT INTO meta_menu_roles (menus, roles) 
VALUES ((SELECT id FROM meta_menu WHERE name = 'contact-root-configuration'), (select currval('auth_role_seq')));

INSERT INTO meta_menu_roles (menus, roles)
VALUES ((SELECT id FROM meta_menu WHERE name = 'contact-root'), (select currval('auth_role_seq')));

INSERT INTO meta_menu_roles (menus, roles) 
VALUES ((SELECT id FROM meta_menu WHERE name = 'contact-root-contacts'), (select currval('auth_role_seq')));

INSERT INTO meta_menu_roles (menus, roles) 
VALUES ((SELECT id FROM meta_menu WHERE name = 'dashboard-root-dashboards'), (select currval('auth_role_seq')));



-- Inserting into auth_role_meta_permissions
INSERT INTO auth_role_meta_permissions (auth_role, meta_permissions) 
VALUES (currval('auth_role_seq'), currval('meta_permission_seq'));
