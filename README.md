# arrow-rest-api

This is a proof of concept REST API using Kotlin, Arrow Kt and Exposed. It tries to only use functional programming concepts to implement a fairly real world case:

- Authentication of user and generation of JWT token.
- List users.
- Create user.
- Authorization of list & creation of user using role based authorization.

Todo:

- Implement Database queries using the IO monad
- Implement the Reader monad for accessing the UseRepository