# Tenant-service
The tenant-service for getting a config file to APIs.

## Project Status
[![Build Status](https://travis-ci.com/speech4j/tenant-service.svg?branch=master)](https://travis-ci.com/speech4j/tenant-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=speech4j_tenant-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=speech4j_tenant-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=speech4j_tenant-service&metric=coverage)](https://sonarcloud.io/dashboard?id=speech4j_tenant-service)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=speech4j_tenant-service&metric=sqale_index)](https://sonarcloud.io/dashboard?id=speech4j_tenant-service)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=speech4j_tenant-service&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=speech4j_tenant-service)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=speech4j_tenant-service&metric=ncloc)](https://sonarcloud.io/dashboard?id=speech4j_tenant-service)


## Requirements

* Java 11
* Docker

## Building Instructions
(If running from windows - replace / to \ )
 * `./gradlew clean build` -- build the project and run the tests.

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
                -p 5432:5432 -d tenant_db
```

## Running DB Migration
(Metadata is a default schema type. To generate a default one - execute command without an additional argument)
 * `./gradlew diffChangeLog -PschemaType=tenant` -- generate a migration script for tenant schema
 * `./gradlew diffChangeLog` -- generate a migration script for metadata schema