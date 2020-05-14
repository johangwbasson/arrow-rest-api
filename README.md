# arrow-rest-api

This is a proof of concept REST API using Kotlin, Arrow Kt and Exposed. It tries to only use functional programming concepts to implement a fairly real world case:

- Authentication of user and generation of JWT token.

```bash
curl --request POST \
  --url http://localhost:7123/api/authenticate \
  --header 'content-type: application/json' \
  --data '{
	"email" : "admin@local.com",
	"password": "admin"
}'
```

- List users.
```bash
curl --request GET \
  --url http://localhost:7123/api/users \
  --header 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiW0FETUlOSVNUUkFUT1IsIFVTRVJdIiwiZXhwIjoxNTg5NDY2MDUwfQ.75rOAj01I459R902UqTWoXJ4TJoyxDI1BFw8mWz9MKk'
```
- Create user.
```bash
curl --request POST \
  --url http://localhost:7123/api/users \
  --header 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiW0FETUlOSVNUUkFUT1IsIFVTRVJdIiwiZXhwIjoxNTg5NDY2MDUwfQ.75rOAj01I459R902UqTWoXJ4TJoyxDI1BFw8mWz9MKk' \
  --header 'content-type: application/json' \
  --data '{
	"email": "john@local.com",
	"password": "password",
	"roles": [ "USER" ]
}'
```

- Authorization of list & creation of user using role based authorization.

Todo:

- Implement Database queries using the IO monad
- Implement the Reader monad for accessing the UseRepository & Secret Key

