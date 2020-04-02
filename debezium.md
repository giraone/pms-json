# Debezium


```
# LIST
select * from pg_replication_slots;
# DELETE
select pg_drop_replication_slot('debezium');
```


```
#CDC# Operation=UPDATE Schema=localhost_pmssql.public.employee.Value
SourceRecord{sourcePartition={server=localhost-pmssql}, sourceOffset={lsn_proc=24942880, lsn=24942880, txId=641, ts_usec=1578930319377535}}
ConnectRecord{topic='localhost-pmssql.public.employee', kafkaPartition=null, key=Struct{id=1151},
value=Struct{after=Struct{id=1151,surname=Wagner,given_name=Hannelore,date_of_birth=1559,gender=FEMALE,postal_code=94542,city=Ansbach,street_address=Am Weg 1,company_id=1001},
source=Struct{version=0.10.0.Final,connector=postgresql,name=localhost-pmssql,ts_ms=1578930319377,db=pmssql,schema=public,table=employee,txId=641,lsn=24942880},
op=u,ts_ms=1578930319402}, timestamp=null,
headers=ConnectHeaders(headers=)} Struct{id=1151}
Struct{after=Struct{id=1151,surname=Wagner,given_name=Hannelore,date_of_birth=1559,gender=FEMALE,postal_code=94542,city=Ansbach,street_address=Am Weg 1,company_id=1001},
source=Struct{version=0.10.0.Final,connector=postgresql,name=localhost-pmssql,ts_ms=1578930319377,db=pmssql,schema=public,table=employee,txId=641,lsn=24942880},
op=u,ts_ms=1578930319402}
```
