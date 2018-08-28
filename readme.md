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

* проверить что правила загрузилсиь можно отправив запрос к сервису маршрутизации

Из консоли

        curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '[ \ 
           { \ 
             "name": "medicalEntityID", \ 
             "value": "99" \ 
           }, \ 
           { \ 
             "name": "roleID", \ 
             "value": "1" \ 
           },   \ 
           { \ 
             "name": "newUI", \ 
             "value": "true" \ 
           } \ 
          \ 
         ]' 'http://localhost:8888/rs/route'

    
Или через swagger

    Перейти http://localhost:8888/swagger-ui.html#!/route-application-service/findRouteUsingPOST
    В body передать
    
        [
          {
            "name": "medicalEntityID",
            "value": "99"
          },
          {
            "name": "roleID",
            "value": "1"
          },  
          {
            "name": "newUI",
            "value": "true"
          }
        ]

В ответ должны получить

        {
          "targetUrl": "/web/accessPoint/index.api",
          ....
        }
