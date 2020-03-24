# Tenant-service
The tenant-service for getting a config file to APIs.

## Requirements

* Java 11
* Docker

## Building Instructions
 * `gradle package` -- create executable jar
 * `gradle clean build` -- build the project

## Testing Instructions
(If running from windows - replace / to \ )
 * `./gradlew clean test` -- build and run the tests.
 
## Running DB Instructions
 * `docker run --name postgres-docker -e POSTGRES_PASSWORD=postgres -e POSTGRES_USERNAME=postgres -e POSTGRES_DB=tenant_db -p 5432:5432 -d postgres` -- create and run db container locally
 
