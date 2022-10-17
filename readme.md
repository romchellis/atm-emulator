# atm-emulator
Next project has 3 maven modules.

atm-client - simple thin REST client which proxies all requests
to bank-core-service

Docker compose has 3 services
1) atm-client - in a host network

2) bank-core - in private network

3) postgres - in private network


## Run


```
docker compose build
docker-compose up
```

## Unimplemented feautures yet

1) Set preferable login method
2) Failover mechanism
3) Https
4) Concurrency control
5) Password encoding