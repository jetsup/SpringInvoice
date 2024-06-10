# Invoice

## Setup

Remember to set your database credentials in the [application.properties](src/main/resources/application.properties).

## Running the Application

This program uses port `8090` to run the server. The server is a RESTful API that allows you to create, read, update,
and
delete invoices. The server is built using Spring Boot and Gradle. The frontend is built using Vue and runs on
port `8080`.
You can run the server and frontend using the following commands:

```shell
$ ./gradlew bootRun
````

The frontend is in the `front-end` directory. The frontend is built using Vue and uses the Vue CLI. The frontend is

```shell
$ cd front-end
$ npm run serve
```

In case there is some linting error in the frontend, you can fix it by running the following command:

```shell
npm run lint -- --fix
```

## Note

The system was tested on Ubuntu 22.04.4 LTS. The system should work on other operating systems, but it was not tested on
them.