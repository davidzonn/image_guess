echo drop database if exists items | C:\xampp\mysql\bin\mysql -u root --password=""
echo create database items | C:\xampp\mysql\bin\mysql -u root --password=""
C:\xampp\mysql\bin\mysql -u root --password="" items < dumpDB.sql
cmd /k