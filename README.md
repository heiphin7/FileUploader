# FileUploader

FileUploader - это RESTful API, разработанное с использованием Spring Boot, включающее модули Spring Data JPA, Security и MVC. Основная цель проекта заключается в создании удобного инструмента для загрузки файлов пользователем, их последующего хранения на сервере в определенных каталогах и сохранения метаданных о файлах в базе данных. Проект настроен на взаймодействие с базой данных, для которого используется Spring Data JPA и ORM (Hibernate). 


## Technologies

Так как проект - RESTful API, у нее нет интерфейса. То есть, **использован только Backend**

## Backend 

+ Java: Основной язык программирования для разработки
+ Spring boot: Фреймворк для создания веб - приложения и REST API
+ Spring Security: Фреймворк для обеспечения аутентификации и авторизации в приложении
+ JWT (Json web token): Механизм для создания и проверки токенов в контексте аутентификации
+ Spring Data JPA:  интерфейсы, которые можно определять для получения доступа к данным
+ Hibernate: ORM (объектно - реляционное отображение), фреймворк для работы с базой данных
+ PostreSQL: Реляционная база данных
+ Lombok: Библиотека, предоставляющая аннотации для сокращениия кода
+ Maven: Инструмент для управления зависимостями проекта


# API Endpoints

- **Загрузка файла**
  - `POST /api/v1/upload-file`
    
- **Аунтентификация**
  - `POST /login`

- **Регистрация нового пользователя**
  - `POST /api/v1/upload-file`

- **Получение файла по id**
  - `GET /api/v1/get/file/{id}`

- **Получение всех файлов пользователя**
  - `PUT /api/v1/user-files/{id}`
    

# Хранение файлов

Файлы хранятся локально (на компе) и пути можно посмотреть в UploadPaths.java , и она имеет следующую структуру:

![alt text](https://github.com/heiphin7/FileUploader/blob/main/file%20hierarchy.png)


# Связи и Табилцы в базе данных

Здесь представлены связи таблиц в базе данных

![alt text](https://github.com/heiphin7/FileUploader/blob/main/database%20diagram.png)
