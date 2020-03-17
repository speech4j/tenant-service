# Tenant-service
The tenant-service for getting a config file to APIs.

## Requirements

* Java 11
* Docker

## Building Instructions

 * `mvnw package` -- build executable jar
 * `docker build .` -- build docker image

## Running Tests
 
 * `./mvnw test` -- all unit tests only
 * `./mvnw integration-test` -- all integration tests only
 * `./mvnw test -Dtest=SignIntegrationTest` -- single test (unit or integration)
 
## Running App

 * `java -jar ./target/tenant-service-fat.jar`
 
### NOTE: set AWS access keys for Integration Tests and Local development
For running integration tests that reach out to AWS Secret Manager set AWS keys to your environment profile.

$ export AWS_ACCESS_KEY_ID='your-aws-key-id'
$ export AWS_SECRET_ACCESS_KEY='your-aws-sercret-key'
$ export AWS_REGION='us-east-1'
