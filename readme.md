## Как запустить проект:


###Сборка проекта и выполнение тестов
* Maven: в корне `mvn install`


###Запуск серверной части:

* Maven: внутри папки application `mvn spring-boot:run`
* или Eclipse: правой кнопкой на файле `ru.emias.router.Application -> debug as Spring Boot App` 

Консоль БД (H2): `http://localhost:8888/h2-console`

Описание сервисов Swagger-UI: `http://localhost:8888/swagger-ui.html#/`

###Запуск пользовательского интерфейса:

* Maven: внутри папки ui `mvn spring-boot:run`. Приложение будет доступно по адресу: `http://localhost:8080/`
* или просто открыть в браузере файл  ui\src\main\resources\static\index.html



###Загрузка в БД набора правил из задания нужно: 

* перейти в консоль БД (H2): `http://localhost:8888/h2-console`. 

		JDBC URL: jdbc:h2:mem:testDb
		
		Username: sa
		
		Password:   (пусто)

* выполнить внутри консоли скрипт: application\src\main\resources\db\scripts\initialLoad.sql 

