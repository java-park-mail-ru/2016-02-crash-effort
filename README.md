# 2016-02-crash-effort
Mastodonts Mail Group (MMG)

Перед запуском нужно настроить пользователя и схему в MySQL. <br />
Для этого можно выполнить команды:

mysql -u root -p < sql/createUser.sql <br />
mysql -u root -p < sql/createSchema.sql <br />

Если вам нужен другой юзер и схема, поменяйте также соответствующие значения в cfg/server.properties

Для запуска выполняем команды:

mvn clean install<br />
./backend.sh
