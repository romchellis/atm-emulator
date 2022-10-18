# atm-emulator
Next project has 3 maven modules.

`atm-client` - simple thin REST client which proxies all requests
to 'bank-core-service'

`bank-core-service` - main backend
`bank-api` - common jar with api

Docker compose has 3 services
1) `atm-client` - in a host network
2) `bank-core` - in private network
3) `postgres` - in private network


## Run


```
mvn install
docker compose build
docker-compose up
```

# Database

Simple bank core has on 5 entities on its backend.
card, card_session, card_login_unsuccessful_attempt, account, client

App works with predefined data
It doesn't support card issue,unblock, add new clients to system and other operations not 
included to requirements

# Session

Session is initiated from frontend and JWT token stores in cookies.
Further this token will be added to request headers between atm and bank
Token TTL - 60 seconds
User should login via /auth/login with 3 attempts in las minute, otherwise his card will blocked

# Failover and exception handling

Atm has circuit breaker in case if bank unreacheble
Atm translates all exceptions from backend
Every exception in application is logged

## Stack

Java 11, Spring boot, JOOQ, Postgres, Swagger, Feign, Hystrix, Junit+Mockito for unit tests account 
operations, JWT token
