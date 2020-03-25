# Tenant-service
The tenant-service for getting a config file to APIs.

## Requirements

* Java 11
* Docker

## Building Instructions
 * `./gradlew package` -- create executable jar
 * `./gradlew clean build` -- build the project

## Testing Instructions
(If running from windows - replace / to \ )
 * `./gradlew clean test` -- build and run the tests.
 
## Running DB Instructions
* create and run db container locally
```
docker run --name postgres-docker \
                -e POSTGRES_PASSWORD=postgres \
                -e POSTGRES_USERNAME=postgres \
                -e POSTGRES_DB=tenant_db \
                -p 5432:5432 -d postgres
```
