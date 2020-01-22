# Taurus
event sourcing based identity governance solution.

## Setup
To make this project work
* specify the docker host in a file called `.env` e.g. `DOCKER_HOST_IP=192.168.99.100`
* change IP in application.yaml to IP of the docker host

## Consumed Events
### OrgNodeChangeEvent
````json
{
  "eventId": "be9045f6-3d55-11ea-b77f-2e728ce88125",
  "action": "CREATE",
  "orgNode": {
    "id": "33e45587-4216-4032-a58c-887648216754",
    "name": "My Root Org"
  }
}
````

## resources

Article
https://dev.to/thegroo/simplest-spring-kafka-producer-and-consumer-kotlin-version-dn8

kafka docker setup description
https://dev.to/thegroo/one-to-run-them-all-1mg6

