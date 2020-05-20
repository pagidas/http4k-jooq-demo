## Yet another demo RESTful web microservice using Kotlin

#### Main technologies used:
- [Http4k](https://www.http4k.org/) - a collection of lightweight libraries written natively in kotlin to handle http.
- [Jooq](https://www.jooq.org/) - a lightweight framework that reads your model in database, generating Java DAOs and POJOs.
- [Flyway](https://flywaydb.org/documentation/migrations) - a lightweight set of tools for database versioning and migrations.

This demo app provides a CRUD api microservice targetting a resource `Item`

##### Routes:

`GET /api/items` gets all items from the database.

`GET /api/items/{id}` gets one item by given id in path variable.

`POST /api/items` creates a new item and stores into the database.

e.g.
```jshelllanguage
{
    "name": "tv stand"
}
```
and response
```jshelllanguage
{
  "id": 1,
  "name": "tv stand"
}
```

`PUT /api/items` updates an existing item in the database, providing that same id with the new name.

e.g.
```jshelllanguage
{
    "id": 1,
    "name": "tv stand"
}
```
and response
```jshelllanguage
{
  "id": 1,
  "name": "tv stand"
}
```

`DELETE /api/items/{id}` removes an existing item from the database given an id in the path variable.

example response:
```jshelllanguage
{
  "message": "Item with given ID: ItemId(value=5) has been deleted."
}
```

#### Testing

Right now, this demo only provides [approval](https://www.http4k.org/guide/modules/approvaltests/) testing for contract.
- Api generated in `OpenApi`
- Each contract of all endpoints

#### Running

This demo has integration with docker through a gradle [plugin](https://github.com/palantir/gradle-docker) which pulls
the generated jar file from `build` directory and sets into an env variable `JAR_FILE` which is picked up by the Dockerfile.
The fatJar is generated through `shadowJar` gradle plugin.

To get you up and running, simply put in terminal: 

`./gradlew generateItemsAppJooqSchemaSource shadowJar docker`

And then, at the root of the project, `docker-compose up`. 

which will pull whatever is needed to orchestrate it (both db, and app)

***On start:*** the app itself runs flyway migrations to create the tables needed in the database.
