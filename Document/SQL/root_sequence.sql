
-- table row increment seq 
create sequence if not exists tbls_dtls.tbls_row_seq
start with 1000
increment by 1
minvalue 1
maxvalue 50000
cache 20
cycle;

-- table seq with date 
select nextval('tbls_dtls.tbls_row_seq') || to_char(current_date, 'ddmmyyyy');


-- table row increment seq 
create sequence if not exists tbls_dtls.tbls_seq
start with 1000
increment by 1
minvalue 1
maxvalue 50000
cache 20
cycle;

-- table seq with date 
select nextval('tbls_dtls.tbls_row_seq') || to_char(current_date, 'ddmmyyyy');