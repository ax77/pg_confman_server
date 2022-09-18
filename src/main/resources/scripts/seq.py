# drop sequence if exists client_client_id_seq;
# create sequence client_client_id_seq increment 1 owned by client.client_id;
# alter table client alter column client_id set default nextval('client_client_id_seq'); 
# select setval('client_client_id_seq', coalesce((select max(client_id) from client), 1));

tables = [
  'auth_privilege               '  
, 'auth_resource                '       
, 'auth_role                    '   
, 'auth_role_resource_privilege '
, 'auth_user                    '      
, 'auth_user_has_roles          '
]

for table in tables:
    
    tabname = table.strip()
    colname = tabname + '_id'
    seqname = tabname + '_idseq'
    print('drop sequence if exists ' + seqname + ';')

print('\n')

for table in tables:
    
    tabname = table.strip()
    colname = tabname + '_id'
    seqname = tabname + '_idseq'
    
    res = '-- ' + tabname + '\n'
    
    # create sequence client_client_id_seq increment 1 owned by client.client_id;
    res = res + 'create sequence ' + seqname + ' increment 1 owned by ' + tabname + '.' + colname + ';\n'
    
    # alter table client alter column client_id set default nextval('client_client_id_seq'); 
    res = res + 'alter table ' + tabname + ' alter column ' + colname + ' set default nextval(\'' + seqname + '\'' + ');\n'
    
    # select setval('client_client_id_seq', coalesce((select max(client_id) from client), 1));
    res = res + 'select setval(\'' + seqname + '\', coalesce((select max(' + colname + ') from ' + tabname + '), 1));\n'

    print(res)
    