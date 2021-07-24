# bookShop

Используемые технологии:

- Java 8
- Maven
- Spring Boot 2.4.5
- Spring Security 5.3.9.RELEASE
- JWT 0.9.1
- Spring Data JPA 2.4.5
- PostgresSQL 42.2.19
- Liquibase 3.10.3
- Springfox 3.0.0
- JAXB 2.3.1
- Selenium 3.141.59
- Twilio 8.14.0
- Spring Boot Mail 2.5.0
- Spring Boot Actuator 2.5.0
- Spring Boot Admin Client 2.4.1
- Spring Boot OAuth2 client 2.5.0
- Apache Commons-io 1.3.2
- Thymeleaf 2.4.5
- Html5
- CSS
- JQery

## Настройки приложения:
### Изменить настройки вы можете в файле MyBookShopApp\src\main\resources\application.properties

_Порт приложения: 8080_ <br>
_Порт базы данных: 5432_ <br>
_Имя пользователя: postgres_ <br>
_Пароль пользователя: postgres_ <br>

База данных инициализируется при запуске приложения с помощью Liquibase, в resources/db/changelog для каждой сущности находится пакет с changeSet b SQL-скриптами, для этого необходимо установить следующие настройки:

_spring.liquibase.enabled=true_ <br>
_spring.liquibase.drop-first=true_ <br>
_spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml_ <br>

Приложение интегрировано с Google Book API и для использования строки поиска необходимо установить следующие свойство:

_google.books.api.key={your key}_

При регистрации нового пользователя на указанные e-mail и номер телефона отсылаются одноразовые коды для подтверждения указанных данных. Аутентификация пользователя проходит одним из предложенных способов, через e-mail или через номер телефона, на которые так же отправляется одноразовый код. <br>
Для отправки одноразовых кодов необходимо установить следующие настройки:

Для телефона:

_twilio.ACCOUNT_SID={your account}_ <br>
_twilio.AUTH_TOKEN={your token}_ <br>
_twilio.TWILIO_NUMBER={your number}_ <br>

Для e-mail:

_appEmail.email={your email}_ <br>
_appEmail.password={your password}_ <br>

Для пополнения счета пользователя приложение интегрированно с платежным сервисом Robocassa, настройки для работы с сервисом в тестовом режиме:

_robokassa.merchant.login={your login}_ <br>
_robokassa.pass.first.test={your password}_

Установка пути хранения обложек книг:

_upload.path={path}_

Установка пути хранения файлов книг:

_download.path={path}_

## Описание запуска приложения

#### Для Windows
Запуск из командной строки из корневой папки: <br>
mvnw spring-boot:run для Windows

Создание и запуск jar файла: <br>
mvnw clean -Dmaven.test.skip package <br>
cd target <br>
java -jar MyBookShopApp-0.0.1-SNAPSHOT.jar

#### Для Linux
Запуск из командной строки из корневой папки: <br>
mvn spring-boot:run для Windows

Создание и запуск jar файла: <br>
mvn clean -Dmaven.test.skip package <br>
java -jar */MyBookShopApp-0.0.1-SNAPSHOT.jar
