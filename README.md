# Current Accounts

## Run locally

### Using gradle
```shell
./gradlew bootRun
```

### Using Docker
```shell
docker run -p 8080:8080 me/ca-app
```

## Build

### Build docker image directly through gradle
```shell
./gradlew bootBuildImage --imageName=me/ca-app
```

### Build docker image through docker
```shell
 docker build -t me/ca-app .
```

## Tests
 - All services have tests using Mocks.
 - Api layer has integration tests.
 - Added postman tests for e2e testing

## Misc

### Assumptions
1. Single Customer can have multiple current accounts
2. Account opening requests are fired by Bank Employee (as the request can initialBalance)

### Improvements
1. Take `accountNumber` as the input to account creation endpoint. So account creation can be made Idempotent. Now Account creation may succeed, but deposit may fail, which can cause zombie accounts. 

### Tools
1. Lombok
   - To handle boilerplate code
   - Prefer builder to multi argument constructor as 2 consecutive fields of same type cause lot of confusion and cause lot of issues. 
2. Annotations
   - NonNull/Nullable annotations to make sure IDE/Lint catches issues with null pointers
3. Money
   - It is a bad practice to use double for representing money. Using Java Money.

