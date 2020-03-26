~~* POST: /tenants - remove id from incoming request~~ 
```
{
    ~~"id": 0,~~
    "name": "string",
    ~~"createdDate": "2020-03-25T16:26:19.609Z",~~
    ~~"active": true~~
  }
```
NOTE: look at [swagger annotations](https://github.com/swagger-api/swagger-core/wiki/Annotations#quick-annotation-overview)

* using swagger annotations provide more responses for each endpoint (response codes and examples)

~~* tenant should be active after creation and not active after removing~~

~~* createDate should be presented as timestamp~~

* **id** should use **UUID** data type (not long or int) - String in DB

~~* remove these fields from create request~~

```
{
  ~~"id": 0,~~
  "firstName": "string", (required)
  "lastName": "string", (required)
  "email": "6666", (required)
  "password": "passwd", (required)
  ~~"role": "ADMIN",~~ (optional)
  ~~"createdDate": "2020-03-25T16:42:27.901Z",~~
  ~~"updatedDate": "2020-03-25T16:42:27.901Z",~~
  ~~"active": true~~
}
```

~~* missed default role when user created without role~~

~~* DTOs: request and response~~

~~* should not return any value with **null**~~

```
[
  {
    "id": 2,
    "firstName": "string",
    "lastName": "string",
    "email": "my@email.com",
    ~~"password": null,~~
    ~~"role": null,~~
    "createdDate": "2020-03-25T16:45:45.400+0000",
    "updatedDate": "2020-03-25T16:45:45.400+0000",
    "active": false
  }
]
```

* missed comments in source code


