Spring Hospital Sample Application 
===============================
Полнофункциональное Spring/JPA Enterprise приложения c авторизацией и правами доступа на основе ролей с использованием наиболее популярных инструментов и технологий Java: Maven, Spring MVC, Security, JPA(Hibernate), REST(Jackson), Bootstrap (css,js), datatables, jQuery + plugins, Java 8 Stream and Time API и хранением в базах данных Postgresql и HSQLDB.

### <a href="http://hospital.herokuapp.com/" target=_blank>Демо разрабатываемого приложения</a>

##![image](https://user-images.githubusercontent.com/56883656/67366078-d97ed680-f583-11e9-99a0-6a5eae5ef0b0.png)

## Описание проекта
В конфигурации по умолчанию Hospital использует профиль DATAJPA и базу данных postgresQL (так же доступны профили jpa, jdbc), которая при запуске заполняется данными. Аналогичная настройка предоставляется для HSQLDB в случае.

Запуск проекта локально
===============
Внутри IntelliJ IDEA В главном меню выберите «Файл» -> «Открыть» и выберите Hospital pom.xml. Для работы приложения необходимо создать переменную окружения TOPJAVA_ROOT и в значении указать путь до проекта "../hospital/". В проекте для запуска проекта используется tomcat. В настройках Deployment в качестве артифакта использовать "war exploded". Приложение будет доступно по адресу http: // localhost: 8080 в вашем браузере
