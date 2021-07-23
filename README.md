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

Порт приложения: 8080 <br>
Порт базы данных: 5432 <br>
Имя пользователя: postgres <br>
Пароль пользователя: postgres <br>

### Изменить настройки вы можете в файле MyBookShopApp\src\main\resources\application.properties

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
