create user 'java'@'localhost' identified by 'password';
grant all privileges on *.* to 'java'@'localhost';
create database AirQuality;
create table analysis(id int AUTO_INCREMENT PRIMARY KEY, Latitude double, Longitude double, observedTime timestamp, parameter varchar(100), unit varchar(200), value double, aqi double, category int);
