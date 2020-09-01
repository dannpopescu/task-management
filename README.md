## A simple REST API for a task management system

This project is a Spring Boot based application exposing a REST API for managing tasks. 
It is a work in progress, and the most recent updates are on the `develop` branch. The 
following technologies are used:
- Spring Boot
- Hibernate
- Hibernate Validator
- MapStruct
- Jackson

### tl;dr — Quickstart

To run the application, follow the next steps:

1. Clone the repository.
2. Switch to the `task-management` folder.
3. Run the application.

```
$ git clone https://github.com/dannpopescu/task-management.git
$ cd task-management
$ ./mvnw spring-boot:run
```

**Please note that some requests are available at the moment only on the `develop` branch. 
Clone it if you want to test all the requests.**

`$ git clone -b develop https://github.com/dannpopescu/task-management.git`

### API Overview

We'll use [HTTPie](https://httpie.org/) for making requests.

#### Create a task

When creating a task, you have to specify its name. Optionally you can specify the project ID that the task belongs
to.

```
$ http POST :9090/tasks name='Buy milk' project=1
```

If created successfully, a `201 CREATED` response code, and the following fields will be returned:

```json
{
    "id": 1,
    "name": "buy milky",
    "project": 3,
    "completed": false,
    "dateCompleted": null,
    "dateCreated": "2020-09-01T13:16:05.752356"
}
```

Possible status codes:
- `201 CREATED` - successfully created a new task
- `400 BAD REQUEST` - validation error (invalid task name, project ID etc.)

#### Get all tasks

```
$ http GET :9090/tasks
```

At the moment, this endpoint doesn't support filtering, so all the tasks in the DB will be returned. There is an
open issue ([#11](https://github.com/dannpopescu/task-management/issues/11)) for fixing it.

#### Get a task by ID

```
$ http GET :9090/tasks/{id}
```

#### Update a task by ID

There are two ways to update a task.

1. Using JSON PATCH format ([RFC 6902](https://tools.ietf.org/html/rfc6902))

```
echo '[{"op": "replace", "path": "/name", "value":"Buy juice"}]' \
| http PATCH :9090/tasks/{id} Content-Type:application/json-patch+json
```

2. Using Merge PATCH format ([RFC 7396](https://tools.ietf.org/html/rfc7396))

```
http PATCH :9090/tasks/{id} Content-Type:application/merge-patch+json name='Buy juice'
```

#### Delete a task by ID

```
http DELETE :9090/tasks/{id}
```

#### Mark a task as completed by ID

```
http PUT :9090/tasks/{id}/completed
```

Note: the request should have a zero content-length.

#### Mark a task as uncompleted by ID

```
http DELETE :9090/tasks/{id}/completed
```
